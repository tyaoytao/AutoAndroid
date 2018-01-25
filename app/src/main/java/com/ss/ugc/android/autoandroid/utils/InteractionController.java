/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ss.ugc.android.autoandroid.utils;

import android.os.SystemClock;
import android.support.test.uiautomator.UiDevice;
import android.view.InputEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.ViewConfiguration;

import com.ss.ugc.android.autoandroid.exceptions.AutoAndroidException;

import static com.ss.ugc.android.autoandroid.utils.ReflectionUtils.getField;
import static com.ss.ugc.android.autoandroid.utils.ReflectionUtils.invoke;
import static com.ss.ugc.android.autoandroid.utils.ReflectionUtils.method;

public class InteractionController {
    private static final String FIELD_NAME = "mInteractionController";

    public static final String METHOD_PERFORM_MULTI_POINTER_GESTURE = "performMultiPointerGesture";
    private static final String CLASS_INTERACTION_CONTROLLER = "android.support.test.uiautomator.InteractionController";
    private static final String METHOD_SEND_KEY = "sendKey";
    private static final String METHOD_INJECT_EVENT_SYNC = "injectEventSync";
    private static final String METHOD_TOUCH_DOWN = "touchDown";
    private static final String METHOD_TOUCH_UP = "touchUp";
    private static final String METHOD_TOUCH_MOVE = "touchMove";
    private static final InteractionController sInstance = new InteractionController();
    private static Object interactionController;

    private static final long REGULAR_CLICK_LENGTH = 100;
    private static final int MOTION_EVENT_INJECTION_DELAY_MILLIS = 5;

    private InteractionController() {
        interactionController = getField(UiDevice.class, FIELD_NAME, Device.getUiDevice());
    }

    public static InteractionController getInstance() {
        return sInstance;
    }

    public boolean sendKey(int keyCode, int metaState) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_SEND_KEY, int.class, int.class), interactionController, keyCode, metaState);
    }

    public boolean injectEventSync(InputEvent event) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_INJECT_EVENT_SYNC, InputEvent.class), interactionController, event);
    }

    public boolean touchDown(int x, int y) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_TOUCH_DOWN, int.class, int.class), interactionController, x, y);
    }

    public boolean touchUp(int x, int y) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_TOUCH_UP, int.class, int.class), interactionController, x, y);
    }

    public boolean touchMove(int x, int y) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_TOUCH_MOVE, int.class, int.class), interactionController, x, y);
    }

    public Boolean performMultiPointerGesture(PointerCoords[][] pcs) throws AutoAndroidException {
        return (Boolean) invoke(method(CLASS_INTERACTION_CONTROLLER, METHOD_PERFORM_MULTI_POINTER_GESTURE, PointerCoords[][].class), interactionController, (Object) pcs);
    }

    public boolean swipe(int downX, int downY, int upX, int upY, int steps, boolean drag) {
        boolean ret = false;
        int swipeSteps = steps;
        double xStep = 0;
        double yStep = 0;

        // avoid a divide by zero
        if(swipeSteps == 0)
            swipeSteps = 1;

        xStep = ((double)(upX - downX)) / swipeSteps;
        yStep = ((double)(upY - downY)) / swipeSteps;

        // first touch starts exactly at the point requested
        ret = touchDown(downX, downY);
        if (drag)
            SystemClock.sleep(ViewConfiguration.getLongPressTimeout());
        for(int i = 1; i < swipeSteps; i++) {
            ret &= touchMove(downX + (int)(xStep * i), downY + (int)(yStep * i));
            if(!ret)
                break;
            // set some known constant delay between steps as without it this
            // become completely dependent on the speed of the system and results
            // may vary on different devices. This guarantees at minimum we have
            // a preset delay.
            SystemClock.sleep(100);
        }
        if (drag)
            SystemClock.sleep(REGULAR_CLICK_LENGTH);
        ret &= touchUp(upX, upY);
        return(ret);
    }
}
