#/bin/bash

#安装测试应用
adb install -r app-debug.apk
adb install -r app-debug-androidTest.apk

#将配置文件push到手机里

adb shell mkdir /sdcard/test/
adb push config.txt /sdcard/test/

#启动火山app
adb shell am start -n com.ss.android.ugc.live/.splash.LiveSplashActivity

#启动性能监测脚本

#cd cpu_mem
#sh cpu_mem.sh com.ss.ugc.android.live 1  &
#cpu_mem_pid=$!
#cd ..


adb shell am instrument -w -r -e class com.ss.ugc.android.autoandroid.ExampleInstrumentedTest#checkPreconditions com.ss.ugc.android.autoandroid.test/android.support.test.runner.AndroidJUnitRunner


#kill -9 $cpu_mem_pid
