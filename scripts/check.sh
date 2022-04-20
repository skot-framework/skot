set -e
set -x
./gradlew viewmodel:jvmTest
./gradlew model:jvmTest
./gradlew viewlegacy:compileDebugAndroidTestSources