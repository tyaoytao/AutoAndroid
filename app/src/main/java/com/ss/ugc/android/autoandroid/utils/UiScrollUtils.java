package com.ss.ugc.android.autoandroid.utils;

import android.graphics.Rect;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.view.accessibility.AccessibilityNodeInfo;

import static com.ss.ugc.android.autoandroid.utils.ReflectionUtils.invoke;
import static com.ss.ugc.android.autoandroid.utils.ReflectionUtils.method;

/**
 * Created by tongyao on 2018/1/18.
 */

public class UiScrollUtils {

    public static void scrollTo(String scrollToString) throws UiObjectNotFoundException {
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().descriptionContains(scrollToString).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().textContains(scrollToString).instance(0));
    }

    public static void scrollDown(int maxSwipes, int step) throws UiObjectNotFoundException {
        Device.getUiDevice();
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(2));
//                uiScrollable.flingToEnd(1);
//
        AccessibilityNodeInfo node = (AccessibilityNodeInfo) invoke(method(UiObject.class, "findAccessibilityNodeInfo", long.class), uiScrollable, 10 * 1000);
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        int downX = 0;
        int downY = 0;
        int upX = 0;
        int upY = 0;

        // scrolling is by default assumed vertically unless the object is explicitly
        // set otherwise by setAsHorizontalContainer()
        int swipeAreaAdjust = (int) (rect.height() * 0.1);
        // scroll vertically: swipe down -> up
        downX = rect.centerX();
        downY = rect.bottom - swipeAreaAdjust;
        upX = rect.centerX();
        upY = rect.top + swipeAreaAdjust;

        InteractionController.getInstance().swipe(downX, downY, upX, upY, 5, true);
    }
}
