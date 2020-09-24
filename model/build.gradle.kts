group = Versions.group
version = Versions.version


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("com.squareup.sqldelight")
}



dependencies {
    implementation("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
//    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation(project(":androidTests"))
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdkVersion(Android.compileSdk)



    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
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
////                baseName = "???"
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
        api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
    }


//    sourceSets["androidMain"].dependencies {
//        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}")
//    }

    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
    }

    sourceSets["androidTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
        implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
    }
//    sourceSets["iosMain"].dependencies {
//        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${Versions.serialization}")
//    }
}

sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
}