rootProject.name = "SKot Framework"
rootProject.buildFileName = "build.gradle.kts"
enableFeaturePreview("GRADLE_METADATA")
include(":core")
include(":contract")
include(":view")
include(":viewmodel")
include(":model")
include("generator")
include(":androidTests")