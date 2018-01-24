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
        public long wait;
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
                Assert.assertEquals(Device.isExistElementByID(argument.value, argument.wait), true);
                break;
            case "className":
                Assert.assertEquals(Device.isExistElementByClassName(argument.value, argument.wait), true);
                break;
            case "text":
                Assert.assertEquals(Device.isExistElementByText(argument.value, argument.wait), true);
                break;
            case "desc":
                Assert.assertEquals(Device.isExistElementByDesc(argument.value, argument.wait), true);
                break;
            case "parentId":
                Assert.assertEquals(Device.isExistElementByParentID(argument.value, argument.child, argument.wait), true);
                break;

        }
        return true;
    }

}
