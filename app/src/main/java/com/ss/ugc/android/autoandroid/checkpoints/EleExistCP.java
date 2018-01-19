package com.ss.ugc.android.autoandroid.checkpoints;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.google.gson.Gson;
import com.ss.ugc.android.autoandroid.exceptions.InvalidElementException;
import com.ss.ugc.android.autoandroid.utils.Device;

/**
 * Created by tongyao on 2018/1/16.
 */

public class EleCheckPoint extends AbsCheckPoint<EleCheckPoint.Argument> {

    static class Argument {
        public String method;
        public String value;
        public String action;
        public String child;
        public String param;
    }

    public EleCheckPoint() {
        super(Argument.class);
    }

    @Override
    public boolean check() throws Exception {
        UiObject element = null;
        switch (argument.method) {
            case "id":
                element = Device.findElementByID(argument.value);
                doAction(argument.action, element);
                break;
            case "parent_id":
                element = Device.findElementByID(argument.value);
                UiObject childElement = findChild(element);
                doAction(argument.action, childElement);
                break;
            case "class_name":
                break;
            case "slip":
                break;
            case "stay":

                break;
        }
        return true;
    }
    private void doCheck() {

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
