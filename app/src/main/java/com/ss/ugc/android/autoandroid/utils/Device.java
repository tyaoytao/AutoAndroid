package com.ss.ugc.android.autoandroid.utils;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.ss.ugc.android.autoandroid.exceptions.InvalidElementException;

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

    public static UiObject findElementByID(String id) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().resourceId(id));
        return uiObject;
    }

    public static boolean isExistElementByID(String id, long ms) {
        return getUiDevice().findObject(new UiSelector().resourceId(id)).waitForExists(ms);
    }

    public static boolean isNotExistElementByID(String id, long ms) {
        return getUiDevice().findObject(new UiSelector().resourceId(id)).waitUntilGone(ms);
    }

    public static UiObject findElementByText(String text) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().text(text));
        return uiObject;
    }

    public static boolean isExistElementByText(String text, long ms) {
        return getUiDevice().findObject(new UiSelector().text(text)).waitForExists(ms);
    }

    public static boolean isNotExistElementByText(String text, long ms) {
        return getUiDevice().findObject(new UiSelector().text(text)).waitUntilGone(ms);
    }

    public static UiObject findElementByDesc(String desc) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().description(desc));
        return uiObject;
    }

    public static boolean isExistElementByDesc(String desc, long ms) {
        return getUiDevice().findObject(new UiSelector().description(desc)).waitForExists(ms);
    }

    public static boolean isNotExistElementByDesc(String desc, long ms) {
        return getUiDevice().findObject(new UiSelector().description(desc)).waitUntilGone(ms);
    }

    public static UiObject findElementByClassName(String className) {
        UiObject uiObject = getUiDevice().findObject(new UiSelector().className(className));
        return uiObject;
    }

    public static boolean isExistElementByClassName(String className, long ms) {
        return getUiDevice().findObject(new UiSelector().className(className)).waitForExists(ms);
    }

    public static boolean isNotExistElementByClassName(String className, long ms) {
        return getUiDevice().findObject(new UiSelector().className(className)).waitUntilGone(ms);
    }

    public static UiObject findElementByParentID(String value, String child) throws InvalidElementException {
        UiObject element = Device.findElementByID(value);
        UiObject childElement = findChild(child, element);
        return childElement;
    }

    public static boolean isExistElementByParentID(String value, String child, long ms) throws InvalidElementException {
        UiObject element = Device.findElementByID(value);
        UiObject childElement = findChild(child, element);
        return childElement.waitForExists(ms);
    }

    public static boolean isNotExistElementByParentID(String value, String child, long ms) throws InvalidElementException {
        UiObject element = Device.findElementByID(value);
        UiObject childElement = findChild(child, element);
        return childElement.waitUntilGone(ms);
    }

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
    public static boolean pressBackKey(){
        return getUiDevice().pressBack();
    }

    public static boolean pressHomeKey() {
        return getUiDevice().pressHome();
    }

    public static void stay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
