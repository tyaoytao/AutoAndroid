
## why?

在测试直播的过程中经常会有测试反复进出直播间，持续下滑进入直播间的性能测试需求，但是往往需要手动去操作大约十几分钟到半个小时，要测试不同机型上的表现更是觉得耗费时间。因此想到是否能用UI自动化解决这一个问题：首先希望降低使用成本；其次，复用性高，下次能直接跑脚本就可以看到结果；同时想做的更通用一些，小视频、抖音、内涵等其他项目也可以使用。

目标是希望在使用时只编写一个测试用例脚本就可以轻松执行出结果。

## 工具调研

最后选择了Android官方支持的自动化框架UIAutomator。它的优势很明确。

* 官方支持，测试依赖环境少，方便创建
* 框架结构清晰，API明晰，上手成本低
* 在事件等待方面接口丰富，表现优秀
* 其他的支持Android的测试框架(比如appium)均基于这个框架开发

官方Demo地址：https://github.com/googlesamples/android-testing/tree/master/ui/uiautomator/BasicSample

性能抓取工具复用我们现有的cpu_mem_collection工具。

## 测试用例配置文件config.txt

首先从配置文件开始，逐行读取配置文件，每一行代表一个动作指令或者是判断元素是否出现。那就需要先定好配置文件格式，从解析文件入手。

第一个位置为该行指令的类型：动作（action）或者是判断元素（checkpoint）

第二个位置为子标签，是元素行为（element）还是系统行为（system，例如点击home键、点击返回键等）

第三个位置就是具体的参数指令。{"method": "string", "value":"string", "action": "string"}，method指的是根据什么方式获得元素：id、classname、text、description等，value是对应的值，action是对元素的行为，包括点击、长按、点击并等待新窗口出现等。在实际中很多元素都不能直接通过id或者classname定位到，就需要父元素向下寻找子元素。定义method为parentId, value为父元素id, child为子元素的位置，例如，"0,3,2"就代表向下第一层的第0个元素，第二层的第3个元素，第三层的第2个元素。

举一个config.txt文件的栗子

```
#点击首页按钮
action: ele_action: {"method": "parentId", "value":"android:id/tabs", "child":"0", "action": "click"}

#停留3s
action: sys_action: {"method": "stay", "value": "3000"}

#点击直播tab
action: ele_action: {"method": "parentId", "value":"com.ss.android.ugc.live:id/pager_sliding_tab_strip", "child":"0,0", "action": "click"}

#检查直播tab下的排行榜是否出现 wait为等待时间
cp: ele_exist: {"method":"id","value":"com.ss.android.ugc.live:id/rank_title", "wait":3000}

#下滑
action: sys_action: {"method": "slipDown", "value": "0,2"}

#点击直播窗口进入直播间
action: ele_action: {"method": "parentId", "value":"com.ss.android.ugc.live:id/feed_list", "child":"1", "action":"click"}

#检查直播间关闭按钮是否出现
cp: ele_exist: {"method": "id", "value": "com.ss.android.ugc.live:id/close", "wait":3000}

#停留10s
action: sys_action: {"method": "stay", "value": "10000"}

#点击直播间的关闭按钮
action: ele_action: {"method": "id", "value":"com.ss.android.ugc.live:id/close", "action":"click"}
```


![](https://github.com/tyaoytao/AutoAndroid/blob/master/art/case.png)

## 实现结构

Action:
主要发出页面交互命令，比如查找某一个元素，然后点击。目前实现了两个Action:

EleAction: 用于UI页面间的交互。method代表查找元素的方法。目前实现了id, text, desc, className, parentId. parentId是一个比较特殊的查找元素的方法。
SysAction：用于执行一些系统命令。比如按返回键，按Home键，滑动等。
CheckPoint
主要用于检查当前页面的元素是否符合要求。比如元素是否存在、是否消失。


![](https://github.com/tyaoytao/AutoAndroid/blob/master/art/structure.png)


## 遇到的坑
### 查找子元素的坑
一开始使用UIObject的getChild通过index去查找子元素，但是返回元素并不是我们要找的元素，一开始的实现方式如下：

public static UiObject findChild(String child, final UiObject element) throws InvalidElementException {
        String[] childStringArray = child.split(",");
        UiObject childElement = element;
        for (String childString : childStringArray) {
            try {
                int childPostion = Integer.valueOf(childString.trim());
                childElement = childElement.getChild(new UiSelector().index(childPostion));
            } catch (Exception e) {
                throw new InvalidElementException("Can't find element for " + childString);
            }
        }
        return childElement;
    }


其中的index方法的官方注释如下：


```
/**
     * Set the search criteria to match the widget by its node
     * index in the layout hierarchy.
     *
     * The index value must be 0 or greater.
     *
     * Using the index can be unreliable and should only
     * be used as a last resort for matching. Instead,
     * consider using the {@link #instance(int)} method.
     *
     * @param index Value to match
     * @return UiSelector with the specified search criteria
     * @since API Level 16
     */
```

而instance方法只能够顺序查找子元素，不是层级的关系找的。

最后通过使用UiObject2解决这一问题，因为UIObject2会实际的对应到具体的页面View，UIObject只是View的呈现，可能会对应到多个View。最后的实现方式：

```
public static UiObject2 findChild(String child, final UiObject2 element) throws InvalidElementException {
    String[] childStringArray = child.split(",");
    UiObject2 childElement = element;
    for (String childString : childStringArray) {
        try {
            int childPosition = Integer.valueOf(childString.trim());
            if (childPosition >= childElement.getChildCount()) {
                return null;
            }
            childElement = childElement.getChildren().get(childPosition);
        } catch (Exception e) {
            throw new InvalidElementException("Can't find element for " + childString);
        }
    }
    return childElement;
}
```

### 实现滑动的坑
UIAutomator系统带的UiScrollable只能够按照指定方向随机性的滑动一下，但是具体滑动多少并无法准确控制。于是查看了flingToEnd的具体实现，发现是使用了InteractionController里面的swipe方法实现滑动操作，但是InteractionController并没有给我们开放Api，于是查看资料后，最终用反射实现。下滑的实现：

```
public static void scrollDown(int instance, float scrollScreenRatio) throws UiObjectNotFoundException {
    UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(instance));
    AccessibilityNodeInfo node = (AccessibilityNodeInfo) invoke(method(UiObject.class, "findAccessibilityNodeInfo", long.class), uiScrollable, 10 * 1000);
    Rect rect = new Rect();
    node.getBoundsInScreen(rect);
    int viewHeight = rect.bottom - rect.top;
    int scrollDefaultDistance = (int) (viewHeight * SCROLL_DISTANCE_RATIO);
    int downX = rect.centerX();
    int downY = rect.bottom - (viewHeight / SCROLL_THRESHOLD);
    int upX = rect.centerX();
    int upY = 0;
    int scrollHeight = (int) (viewHeight * scrollScreenRatio);
    if ((viewHeight / 2) > scrollHeight) {
        upY = downY - scrollHeight;
        InteractionController.getInstance().swipe(downX, downY, upX, upY, 10, true);
    } else {
        downY = rect.bottom - (viewHeight / SCROLL_THRESHOLD);
        while(scrollHeight > 0) {
            int distance = (scrollDefaultDistance > scrollHeight? scrollHeight : scrollDefaultDistance);
            upY = downY - distance;
            InteractionController.getInstance().swipe(downX, downY, upX, upY, 10, false);
            scrollHeight = scrollHeight - distance;
        }
    }
}
```


### 不能点击的坑

在vivo x9plus上执行click()报如下错误：

Injecting to another application requires INJECT_EVENTS permission

查到是因为没有模拟点击的权限，解决方法就是在开发者选项 → USB模拟点击的开关打开就可以了。

### 找不到元素的坑
找元素的方法findObject()需要在当前界面的UI线程已经空闲的时候才会去做，火山右下角有一个查看帧率的小圆窗一直在占用UI线程，导致找不到元素报超时错误，解决方法：双击消灭掉这个小圆窗就可以了。

## 使用方法

1. 用uiautomatorviewer查看页面元素布局，编写测试脚本config.txt

2. 在deploy目录下运行luncher.sh，参数为开屏页的Activity:   sh luncher.sh com.ss.android.ugc.live/.splash.LiveSplashActivity 

3. 结果在cpu_mem/reports中的 *.html



