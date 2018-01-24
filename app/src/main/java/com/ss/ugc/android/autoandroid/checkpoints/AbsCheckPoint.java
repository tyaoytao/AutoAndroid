package com.ss.ugc.android.autoandroid.checkpoints;

import android.support.test.uiautomator.UiObject;

import com.google.gson.Gson;
import com.ss.ugc.android.autoandroid.core.IRunUnit;

import junit.framework.Assert;

/**
 * Created by tongyao on 2018/1/5.
 */

public abstract class AbsCheckPoint<T> implements IRunUnit {
    static Gson gson = new Gson();
    Class<T> clazz;
    T argument;

    public AbsCheckPoint(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract boolean check() throws Exception;

    @Override
    public void setArgument(String argument) {
        this.argument = gson.fromJson(argument, clazz);
    }

    public void doCheck(UiObject object) {
        Assert.assertEquals(object.exists(), true);
    }
}
