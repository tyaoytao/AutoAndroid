package com.ss.ugc.android.autoandroid.actions;

import com.ss.ugc.android.autoandroid.utils.Device;

/**
 * Created by tongyao on 2018/1/7.
 */

public class SysAction extends AbsAction<SysAction.Argument>{

    static class Argument {
        public String method;
    }

    public SysAction(String params) {
        super(Argument.class);
        setArgument(params);
    }

    @Override
    public void run() throws Throwable {
        switch (argument.method) {
            case "press_back":
                Device.pressBackKey();
                break;
            case "press_home":
                Device.pressHomeKey();
                break;
            case "slip":
                Device.scrollDown(1, 1);
                break;
        }
    }
}
