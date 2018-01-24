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
            case "text":
                element = Device.findElementByText(argument.value);
                doAction(argument.action, element);
                break;
            case "desc":
                element = Device.findElementByDesc(argument.value);
                doAction(argument.action, element);
                break;
            case "className":
                element = Device.findElementByClassName(argument.value);
                doAction(argument.action, element);
                break;
            case "parentId":
                element = Device.findElementByParentID(argument.value, argument.child);
                doAction(argument.action, element);
                break;

        }
    }

    private void doAction(String action, UiObject element) throws UiObjectNotFoundException {
        switch (action) {
            case "click":
                element.click();
                break;
            case "longClick":
                element.longClick();
                break;
            case "clickAndWaitForNewWindow":
                element.clickAndWaitForNewWindow(); //默认5500ms
        }
    }

}
