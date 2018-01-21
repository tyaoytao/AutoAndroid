package com.ss.ugc.android.autoandroid.checkpoints;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.ss.ugc.android.autoandroid.exceptions.InvalidElementException;
import com.ss.ugc.android.autoandroid.utils.Device;

import junit.framework.Assert;

/**
 * Created by tongyao on 2018/1/16.
 */

public class EleExistCP extends AbsCheckPoint<EleExistCP.Argument> {

    static class Argument {
        public String method;
        public String value;
        public String child;
    }

    public EleExistCP(String params) {
        super(Argument.class);
        setArgument(params);
    }

    @Override
    public boolean check() throws Exception {
        UiObject element = null;
        switch (argument.method) {
            case "id":
                element = Device.findElementByID(argument.value);
                doCheck(element);
                break;
            case "parent_id":
                element = Device.findElementByID(argument.value);
                UiObject childElement = findChild(element);
                doCheck(childElement);
                break;
            case "class_name":
                break;
        }
        return true;
    }

    private void doCheck(UiObject object) {
        Assert.assertEquals(object.exists(), true);
    }

    private UiObject findChild(final UiObject element) throws InvalidElementException {
        String[] childStringArray = argument.child.split(",");
        UiObject childElement = element;
        for (String childString : childStringArray) {
            try {
                int child = Integer.valueOf(childString.trim());
                childElement = childElement.getChild(new UiSelector().index(child));
            } catch (Exception e) {
                throw new InvalidElementException("Can'tfind element for " + childString);
            }
        }
        return childElement;
    }


}
