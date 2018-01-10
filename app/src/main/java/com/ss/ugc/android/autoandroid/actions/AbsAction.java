package com.ss.ugc.android.autoandroid.actions;

import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.Gson;
import com.ss.ugc.android.autoandroid.core.IRunUnit;

/**
 * Created by tongyao on 2018/1/5.
 */

public abstract class AbsAction<T> implements IRunUnit {
    static Gson gson = new Gson();
    Class<T> clazz;
    T argument;

    public AbsAction(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract void run() throws Throwable;


    @Override
    public void setArgument(String argument) {
        this.argument = gson.fromJson(argument, clazz);
    }
}
