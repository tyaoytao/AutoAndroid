package com.ss.ugc.android.autoandroid.utils;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

/**
 * Created by tongyao on 2018/1/5.
 */
public abstract class Device {

    public static final UiDevice getUiDevice() {
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static void wake() throws RemoteException {
        getUiDevice().wakeUp();
    }

    public static void scrollTo(String scrollToString) throws UiObjectNotFoundException {
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().descriptionContains(scrollToString).instance(0));
        uiScrollable.scrollIntoView(new UiSelector().textContains(scrollToString).instance(0));
    }

    public static void scrollDown(int maxSwipes, int step)  throws UiObjectNotFoundException {
        getUiDevice();
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));
        uiScrollable.flingToEnd(1);
    }

    public static boolean back() {
        return getUiDevice().pressBack();
    }


    public static UiObject findElementByID(String id) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().resourceId(id));
        return uiObject;
    }

    public static UiObject findElementByText(String text) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().text(text));
        return uiObject;
    }

    public static UiObject findElementByDesc(String desc) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().description(desc));
        return uiObject;
    }

    public static UiObject findElementByClassName(String className) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().className(className));
        return uiObject;
    }

    public static boolean pressBackKey(){
        return getUiDevice().pressBack();
    }

    public static boolean pressHomeKey() {
        return getUiDevice().pressHome();
    }
}
