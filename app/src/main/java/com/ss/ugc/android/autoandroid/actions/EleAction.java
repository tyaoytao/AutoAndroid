package com.ss.ugc.android.autoandroid.actions;

import android.support.annotation.Nullable;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.RelativeLayout;

import com.ss.ugc.android.autoandroid.exceptions.InvalidElementException;
import com.ss.ugc.android.autoandroid.utils.Device;

/**
 * Created by tongyao on 2018/1/5.
 */

public class EleAction extends AbsAction<EleAction.Argument> {
    static class Argument {
        public String method;
        public String value;
        public String action;
        public String child;
        public String param;
    }

    public EleAction(String params) {
        super(Argument.class);
        setArgument(params);
    }

    @Override
    public void run() throws Exception {
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
    }

    private void doAction(String action, UiObject element) throws UiObjectNotFoundException {
        switch (action) {
            case "click":
                element.click();
                break;
            case "longclick":
                //TODO
                break;
        }
    }

    private UiObject findChild(final UiObject element) throws InvalidElementException {
        String[] childStringArray = argument.child.split(",");
        UiObject childElement = element;
        for (String childString : childStringArray) {
            try {
                int child = Integer.valueOf(childString.trim());
                childElement = childElement.getChild(new UiSelector().index(child));
            } catch (Exception e) {
                throw new InvalidElementException("Can't find element for " + childString);
            }
        }
        return childElement;
    }
}
