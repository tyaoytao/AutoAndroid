package com.ss.ugc.android.autoandroid;

import com.ss.ugc.android.autoandroid.checkpoints.AbsCheckPoint;
import com.ss.ugc.android.autoandroid.checkpoints.EleExistCP;

/**
 * Created by tongyao on 2018/1/5.
 */

public final class CheckPointFactory {
    public static AbsCheckPoint getCheckPointByName(String name, String params) {
        switch (name) {
            case "ele_exist":
                return new EleExistCP(params);
            default:
                return null;
        }
    }
}
