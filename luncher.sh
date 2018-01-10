#/bin/bash

sh ./deploy.sh

adb install -r ./deploy/AutoAndroid.apk

adb shell am start -n com.ss.ugc.android.autoandroid/.MainActivity
# 等待1秒，让权限申请的Activity启动并且申请权限
sleep 1

while [ `adb shell dumpsys activity top | grep GrantPermissionsActivity | wc -l` -eq 1 ]; do
    #开始等待赋予权限
    sleep 1
done

adb push ./deploy/config.txt /sdcard/Download/

#adb shell am instrument -w -r   -e debug false -e class com.ss.ugc.android.autoandroid.testrun.InstrumentedTest#checkPreconditions   com.ss.ugc.android.autoandroid/android.support.test.runner.AndroidJUnitRunner


