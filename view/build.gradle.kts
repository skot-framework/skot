group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("com.github.ben-manes.versions")
}


dependencies {
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
    api("androidx.constraintlayout:constraintlayout:${Versions.Android.constraintLayout}")
    api("androidx.viewpager2:viewpager2:${Versions.Android.viewpager2}")
    api("androidx.recyclerview:recyclerview:${Versions.Android.recyclerview}")
    api("com.google.android.material:material:${Versions.Android.material}")

}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").java.srcDirs("generated/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

    buildFeatures {
        viewBinding = true
    }

//    packagingOptions {
//        exclude("META-INF/*.kotlin_module")
//        exclude("META-INF/*")
//    }
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
//
//    iOSTarget("ios") {
////        binaries {
////            framework {
////                baseName = "OufHP"
////            }
////        }
//    }

    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        implementation(project(":contract"))
    }


    sourceSets["androidMain"].dependencies {
    }


}