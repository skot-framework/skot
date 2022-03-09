set -e
set -x
echo "run viewmodel tests"
./gradlew viewmodel:jvmTest
echo "run model tests"
./gradlew model:jvmTest
echo "compile view android tests"
./gradlew viewlegacy:compileDebugAndroidTestSources