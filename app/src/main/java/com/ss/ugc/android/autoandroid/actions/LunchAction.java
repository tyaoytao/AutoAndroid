package com.ss.ugc.android.autoandroid.actions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.ss.ugc.android.autoandroid.utils.Device;

/**
 * Created by tongyao on 2018/1/5.
 */

public class LunchAction extends AbsAction<LunchAction.Argument> {
    public LunchAction(String params) {
        super(Argument.class);
        setArgument(params);
    }

    static class Argument {
        @SerializedName("package")
        public String mPackage;
    }

    @Override
    public void run() {
        Device.getUiDevice().pressHome();
        final String launcherPackage = getLauncherPackageName();
//        assertThat(launcherPackage, notNullValue());
        Device.getUiDevice().wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000);
        final Intent intent = InstrumentationRegistry.getContext().getPackageManager()
                .getLaunchIntentForPackage(argument.mPackage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        InstrumentationRegistry.getContext().startActivity(intent);
    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
