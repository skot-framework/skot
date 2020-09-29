group = Versions.group
version = Versions.version


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.squareup.sqldelight")
}



dependencies {
    implementation("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
    androidTestUtil("androidx.test:orchestrator:1.3.0")
    androidTestImplementation(project(":androidTests"))
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments = mapOf(
                "clearPackageData" to "true"
        )
        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }

    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
    }

//    packagingOptions {
//        exclude("META-INF/*.kotlin_module")
//        exclude("META-INF/*")
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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