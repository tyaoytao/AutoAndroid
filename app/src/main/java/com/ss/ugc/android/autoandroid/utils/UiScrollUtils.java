package com.ss.ugc.android.autoandroid.utils;

import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

/**
 * Created by tongyao on 2018/1/18.
 */

public class UiScrollUtils {

    public static void scrollTo(String scrollToString) throws UiObjectNotFoundException {
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().descriptionContains(scrollToString).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().textContains(scrollToString).instance(0));
    }

    public static void scrollDown(int maxSwipes, int step)  throws UiObjectNotFoundException {
        Device.getUiDevice();
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.flingToEnd(1);
    }
}
