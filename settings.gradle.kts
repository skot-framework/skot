pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:4.0.1")
            }
        }
    }
}
rootProject.name = "SKot Framework"
rootProject.buildFileName = "build.gradle.kts"
include(":core")
//include(":contract")
include(":viewlegacy")
//include(":viewcompose")
//include(":viewmodel")
include(":model")
//include("generator")
//include(":androidTests")
enableFeaturePreview("GRADLE_METADATA")