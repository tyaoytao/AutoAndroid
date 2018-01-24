package com.ss.ugc.android.autoandroid.checkpoints;

import android.support.test.uiautomator.UiObject;

import com.ss.ugc.android.autoandroid.utils.Device;

import junit.framework.Assert;

/**
 * Created by tongyao on 2018/1/24.
 */

public class EleGoneCP extends AbsCheckPoint<EleGoneCP.Argument> {

    static class Argument {
        public String method;
        public String value;
        public String child;
        public long wait;
    }

    public EleGoneCP(String params) {
        super(EleGoneCP.Argument.class);
        setArgument(params);
    }

    @Override
    public boolean check() throws Exception {
        UiObject element = null;
        switch (argument.method) {
            case "id":
                Assert.assertEquals(Device.isNotExistElementByID(argument.value, argument.wait), true);
                break;
            case "className":
                Assert.assertEquals(Device.isNotExistElementByClassName(argument.value, argument.wait), true);
                break;
            case "text":
                Assert.assertEquals(Device.isNotExistElementByText(argument.value, argument.wait), true);
                break;
            case "desc":
                Assert.assertEquals(Device.isNotExistElementByDesc(argument.value, argument.wait), true);
                break;
            case "parentId":
                Assert.assertEquals(Device.isNotExistElementByParentID(argument.value, argument.child, argument.wait), true);
                break;
        }
        return true;
    }

}
