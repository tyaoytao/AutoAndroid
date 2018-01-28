package com.ss.ugc.android.autoandroid.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by tongyao on 2018/1/27.
 */

public abstract class ScreenUtils {

    /**
     * 获得屏幕宽高
     * @return
     */
    public static int[] getScreenSize() {
        Context context = DeviceUtils.getContext();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[]{width, height};
    }
}
