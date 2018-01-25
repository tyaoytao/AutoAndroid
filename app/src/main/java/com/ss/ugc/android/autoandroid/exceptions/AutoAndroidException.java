package com.ss.ugc.android.autoandroid.exceptions;

/**
 * Created by yutao on 2018/1/24.
 */

public class AutoAndroidException extends RuntimeException {
    public AutoAndroidException(String message) {
        super(message);
    }

    public AutoAndroidException(Throwable t) {
        super(t);
    }

    public AutoAndroidException(String message, Throwable t) {
        super(message, t);
    }
}
