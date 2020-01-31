group = Versions.group
version = Versions.version


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}



dependencies {
    api("com.jakewharton.timber:timber:${Versions.Android.timber}")
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

//    //select iOS target platform depending on the Xcode environment variables
//    val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
//            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
//                ::iosArm64
//            else
//                ::iosX64

//    iOSTarget("ios") {
////        binaries {
////            framework {
////                baseName = "OufHP"
////            }
////        }
//    }


    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutines}")
        api(kotlin("reflect"))

    }


    sourceSets["androidMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    }

//    sourceSets["iosMain"].dependencies {
//
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.kotlinCoroutines}")
//    }

}
