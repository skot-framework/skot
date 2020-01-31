group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}



dependencies {
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }


}


kotlin {

//    //select iOS target platform depending on the Xcode environment variables
//    val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
//            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
//                ::iosArm64
//            else
//                ::iosX64
//
//    iOSTarget("ios") {
////        binaries {
////            framework {
////                baseName = "OufHP"
////            }
////        }
//    }

    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }



    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        implementation(project(":contract"))
        api("com.soywiz.korlibs.klock:klock:${Versions.klock}")
    }


    sourceSets["androidMain"].dependencies {
    }



}