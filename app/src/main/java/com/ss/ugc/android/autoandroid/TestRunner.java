package com.ss.ugc.android.autoandroid;

import com.ss.ugc.android.autoandroid.actions.AbsAction;
import com.ss.ugc.android.autoandroid.checkpoints.AbsCheckPoint;
import com.ss.ugc.android.autoandroid.core.IRunUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tongyao on 2018/1/5.
 */

public class TestRunner {
    List<IRunUnit> runList = new ArrayList<>();

    public void addAction(AbsAction action) {
        runList.add(action);
    }

    public void addCheckPoint(AbsCheckPoint checkPoint) {
        runList.add(checkPoint);
    }

    public void run() throws Throwable {
        AbsAction action;
        AbsCheckPoint checkPoint;
        for (IRunUnit unit : runList) {
            if (unit instanceof AbsAction) {
                action = (AbsAction) unit;
                action.run();
            } else if (unit instanceof AbsCheckPoint) {
                checkPoint = (AbsCheckPoint) unit;
                checkPoint.check();
            }
        }
    }
}
