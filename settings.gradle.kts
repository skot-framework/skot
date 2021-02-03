rootProject.name = "SKot Framework"
rootProject.buildFileName = "build.gradle.kts"
include(":core")
include(":viewcontract")
include(":model")
include(":viewmodel")
include("viewlegacy")
include(":androidTests")
include(":plugin")
include(":generator")
//include(":generator")
enableFeaturePreview("GRADLE_METADATA")