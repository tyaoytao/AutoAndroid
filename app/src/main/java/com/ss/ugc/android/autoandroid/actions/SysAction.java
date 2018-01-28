package com.ss.ugc.android.autoandroid.actions;

import com.ss.ugc.android.autoandroid.utils.DeviceUtils;
import com.ss.ugc.android.autoandroid.utils.UiScrollUtils;

/**
 * Created by tongyao on 2018/1/7.
 */

public class SysAction extends AbsAction<SysAction.Argument>{

    static class Argument {
        public String method;
        public String value;
    }

    public SysAction(String params) {
        super(Argument.class);
        setArgument(params);
    }

    @Override
    public void run() throws Throwable {
        switch (argument.method) {
            case "pressBack":
                DeviceUtils.pressBackKey();
                break;
            case "pressHome":
                DeviceUtils.pressHomeKey();
                break;
            case "keyCode":
                DeviceUtils.pressKeyCode(Integer.valueOf(argument.value));
            case "slipDown":
                String[] argValue = argument.value.split(",");
                int instance = Integer.valueOf(argValue[0]);
                float scrollRatio = Float.valueOf(argValue[1]);
                UiScrollUtils.scrollDown(instance, scrollRatio);
                break;
            case "stay":
                DeviceUtils.stay(Integer.valueOf(argument.value));
                break;
        }
    }
}
