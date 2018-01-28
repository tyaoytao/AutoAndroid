package com.ss.ugc.android.autoandroid.checkpoints;

import android.support.test.uiautomator.UiObject;

import com.ss.ugc.android.autoandroid.utils.DeviceUtils;

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
                Assert.assertEquals(DeviceUtils.isExistElementByID(argument.value, argument.wait), true);
                break;
            case "className":
                Assert.assertEquals(DeviceUtils.isExistElementByClassName(argument.value, argument.wait), true);
                break;
            case "text":
                Assert.assertEquals(DeviceUtils.isExistElementByText(argument.value, argument.wait), true);
                break;
            case "desc":
                Assert.assertEquals(DeviceUtils.isExistElementByDesc(argument.value, argument.wait), true);
                break;
            case "parentId":
                Assert.assertEquals(DeviceUtils.isExistElementByParentID(argument.value, argument.child, argument.wait), true);
                break;

        }
        return true;
    }

}
