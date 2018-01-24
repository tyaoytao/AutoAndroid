#/bin/bash

adb install -r ./AutoAndroid.apk


adb shell am start -n com.ss.ugc.android.autoandroid/.MainActivity
# 等待1秒，让权限申请的Activity启动并且申请权限
sleep 1

while [ `adb shell dumpsys activity top | grep GrantPermissionsActivity | wc -l` -eq 1 ]; do
    #开始等待赋予权限
    sleep 1
done

adb push ./config.txt /sdcard/Download/

adb shell am start -n com.ss.android.ugc.live/.splash.LiveSplashActivity
# Lunch cpu_mem
cd cpu_mem
if [ ! -d ./reports ]; then
	mkdir ./reports
fi
cp jscharts.js ./reports
sh ./cpu_mem.sh com.ss.android.ugc.live 1  &
cpu_mem_pid=$!
cd ..

adb shell am instrument -w -r   -e debug false -e class com.ss.ugc.android.autoandroid.testrun.InstrumentedTest#checkPreconditions   com.ss.ugc.android.autoandroid/android.support.test.runner.AndroidJUnitRunner

kill -9 $cpu_mem_pid
