#/bin/bash

num=10

#安装测试应用
adb install -r app-debug.apk
adb install -r app-debug-androidTest.apk

#将配置文件push到手机里
#TODO 判断文件夹是否存在

adb shell mkdir /sdcard/test/
adb push config.txt /sdcard/test/

#启动火山app
adb shell am start -n com.ss.android.ugc.live/.splash.LiveSplashActivity

#启动性能监测脚本
#TODO 异步操作
#sh cpu_mem.sh com.ss.ugc.android.live 1 &
cd cpu_mem
sh ./cpu_mem.sh com.ss.ugc.android.live 1  &
cpu_mem_pid=$!
echo "pid:$cpu_mem_pid"
cd ..
#循环执行测试用例
adb shell am instrument -w -r -e class com.ss.ugc.android.autoandroid.ExampleInstrumentedTest#checkPreconditions com.ss.ugc.android.autoandroid.test/android.support.test.runner.AndroidJUnitRunner


kill -9 $cpu_mem_pid
