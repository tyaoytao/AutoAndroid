#/bin/bash
./gradlew assembleDebug && \
cp ./app/build/outputs/apk/debug/app-debug.apk ./deploy/AutoAndroid.apk


