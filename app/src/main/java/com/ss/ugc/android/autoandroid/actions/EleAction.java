package com.ss.ugc.android.autoandroid.actions;

import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.ss.ugc.android.autoandroid.utils.DeviceUtils;

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
        UiObject2 element = null;
        switch (argument.method) {
            case "id":
                element = DeviceUtils.findElementByID(argument.value);
                doAction(argument.action, element);
                break;
            case "text":
                element = DeviceUtils.findElementByText(argument.value);
                doAction(argument.action, element);
                break;
            case "desc":
                element = DeviceUtils.findElementByDesc(argument.value);
                doAction(argument.action, element);
                break;
            case "className":
                element = DeviceUtils.findElementByClassName(argument.value);
                doAction(argument.action, element);
                break;
            case "parentId":
                element = DeviceUtils.findElementByParentID(argument.value, argument.child);
                doAction(argument.action, element);
                break;

        }
    }

    private void doAction(String action, UiObject2 element) throws UiObjectNotFoundException {
        if (element == null) {
            throw new UiObjectNotFoundException("element not exist");
        }
        switch (action) {
            case "click":
                element.click();
                break;
            case "longClick":
                element.longClick();
                break;
            case "clickAndWaitForNewWindow":
//                element.clickAndWaitForNewWindow(); //默认5500ms
        }
    }

}
