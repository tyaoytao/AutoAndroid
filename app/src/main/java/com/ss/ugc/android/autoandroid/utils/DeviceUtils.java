package com.ss.ugc.android.autoandroid.utils;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;

import com.ss.ugc.android.autoandroid.exceptions.InvalidElementException;

/**
 * Created by tongyao on 2018/1/5.
 */
public abstract class DeviceUtils {
    private static UiDevice sDevice = null;

    static {
        sDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static final Context getContext() {
        return InstrumentationRegistry.getContext();
    }

    public static final UiDevice getUiDevice() {
        return sDevice;
    }

    public static void wake() throws RemoteException {
        getUiDevice().wakeUp();
    }

    public static UiObject2 findElementByID(String id) {
        return getUiDevice().findObject(By.res(id));
    }

    public static boolean isExistElementByID(String id, long ms) {
        return getUiDevice().findObject(new UiSelector().resourceId(id)).waitForExists(ms);
    }

    public static boolean isNotExistElementByID(String id, long ms) {
        return getUiDevice().findObject(new UiSelector().resourceId(id)).waitUntilGone(ms);
    }

    public static UiObject2 findElementByText(String text) {
        return getUiDevice().findObject(By.text(text));
    }

    public static boolean isExistElementByText(String text, long ms) {
        return getUiDevice().findObject(new UiSelector().text(text)).waitForExists(ms);
    }

    public static boolean isNotExistElementByText(String text, long ms) {
        return getUiDevice().findObject(new UiSelector().text(text)).waitUntilGone(ms);
    }

    public static UiObject2 findElementByDesc(String desc) {
        return getUiDevice().findObject(By.desc(desc));
    }

    public static boolean isExistElementByDesc(String desc, long ms) {
        return getUiDevice().findObject(new UiSelector().description(desc)).waitForExists(ms);
    }

    public static boolean isNotExistElementByDesc(String desc, long ms) {
        return getUiDevice().findObject(new UiSelector().description(desc)).waitUntilGone(ms);
    }

    public static UiObject2 findElementByClassName(String className) {
        return getUiDevice().findObject(By.clazz(className));
    }

    public static boolean isExistElementByClassName(String className, long ms) {
        return getUiDevice().findObject(new UiSelector().className(className)).waitForExists(ms);
    }

    public static boolean isNotExistElementByClassName(String className, long ms) {
        return getUiDevice().findObject(new UiSelector().className(className)).waitUntilGone(ms);
    }

    public static UiObject2 findElementByParentID(String value, String child) throws InvalidElementException {
        UiObject2 element = DeviceUtils.findElementByID(value);
        return findChild(child, element);
    }

    public static boolean isExistElementByParentID(String value, String child, long timeoutMs) throws InvalidElementException {
        UiObject2 element = DeviceUtils.findElementByID(value);
        UiObject2 childElement = findChild(child, element);
        long startTime = SystemClock.uptimeMillis();
        // 循环等待找元素
        for (long elapsedTime = 0; childElement == null; elapsedTime = SystemClock.uptimeMillis() - startTime) {
            if (element == null) {
                element = DeviceUtils.findElementByID(value);
            }
            childElement = findChild(child, element);
            if (elapsedTime >= timeoutMs) {
                break;
            }

            SystemClock.sleep(1000);
        }
        return childElement != null;
    }

    public static boolean isNotExistElementByParentID(String value, String child, long timeoutMs) throws InvalidElementException {
        UiObject2 element = DeviceUtils.findElementByID(value);
        UiObject2 childElement = findChild(child, element);
        long startTime = SystemClock.uptimeMillis();
        // 循环等待元素消失掉
        for (long elapsedTime = 0; childElement != null; elapsedTime = SystemClock.uptimeMillis() - startTime) {
            childElement = findChild(child, element);
            if (elapsedTime >= timeoutMs) {
                break;
            }

            SystemClock.sleep(500);
        }
        return childElement == null;
    }

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

    public static boolean pressBackKey() {
        return getUiDevice().pressBack();
    }

    public static boolean pressHomeKey() {
        return getUiDevice().pressHome();
    }

    public static boolean pressKeyCode(int keyCode) {
        return getUiDevice().pressKeyCode(keyCode);
    }

    public static void stay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
