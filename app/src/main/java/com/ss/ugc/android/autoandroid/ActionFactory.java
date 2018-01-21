package com.ss.ugc.android.autoandroid;

import android.support.annotation.Nullable;

import com.ss.ugc.android.autoandroid.actions.AbsAction;
import com.ss.ugc.android.autoandroid.actions.EleAction;
import com.ss.ugc.android.autoandroid.actions.SysAction;
import com.ss.ugc.android.autoandroid.exceptions.ActionNotFoundException;

/**
 * Created by tongyao on 2018/1/5.
 */

public final class ActionFactory {
    @Nullable
    public static AbsAction getActionByName(String name, String params) throws ActionNotFoundException {
        switch (name) {
            case "ele_action":
                return new EleAction(params);
            case "sys_action":
                return new SysAction(params);
            default:
                throw new ActionNotFoundException();
        }
    }
}
