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
    private static final int SCROLL_THRESHOLD = 8; // Must lager than 2
    private static final float SCROLL_DISTANCE_RATIO = 1f - (1f / SCROLL_THRESHOLD) * 2f;
    static {
        DeviceUtils.getUiDevice();
    }

    public static void scrollTo(String scrollToString) throws UiObjectNotFoundException {
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().descriptionContains(scrollToString).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().textContains(scrollToString).instance(0));
    }

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
}
