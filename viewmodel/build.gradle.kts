
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}

group = Versions.group
version = Versions.version


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



    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

//    android()

    ios {
        binaries {
            framework {
                baseName = "sk-viewmodel"
            }
        }
    }


//    val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
//            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
//                ::iosArm64
//            else
//                ::iosX64
//
//    iOSTarget("ios") {
//        binaries {
//            framework {
//                baseName = "sk-viewmodel"
//            }
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                implementation(project(":contract"))
            }
        }
    }



}