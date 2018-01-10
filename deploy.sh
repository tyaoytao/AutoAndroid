#/bin/bash
./gradlew assembleDebug && \
cp ./app/build/outputs/apk/debug/app-debug.apk ./deploy/ && \
./gradlew assembleDebugAndroidTest && \
cp app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk ./deploy/


