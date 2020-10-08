plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.squareup.sqldelight")
}

group = Versions.group
version = Versions.version



kotlin {

android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }

//    android()

    ios {
        binaries {
            framework {
                baseName = "sk-model"
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
//                baseName = "sk-model"
//            }
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                implementation(project(":contract"))
                api("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-json:${Versions.ktor}")
                api("io.ktor:ktor-client-logging:${Versions.ktor}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:${Versions.Android.timber}")
                api("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")

                api("io.ktor:ktor-client-android:${Versions.ktor}")
//                api("io.ktor:ktor-client-json-jvm:${Versions.ktor}")
//                api("io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")
//                api("io.ktor:ktor-client-logging-jvm:${Versions.ktor}")
            }
        }

        val iosMain by getting {
            dependencies {
                api("com.squareup.sqldelight:native-driver:${Versions.sqldelight}")

                api("io.ktor:ktor-client-ios:${Versions.ktor}")
            }
        }


//        val commonTest by getting {
//            dependencies {
//                implementation("org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}")
//                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
//            }
//        }

//        val androidTest by getting {
//            implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
//            implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
//        }

//        val iosTest by getting

    }


}

//dependencies {
//
//    androidTestUtil("androidx.test:orchestrator:1.3.0")
//    androidTestImplementation(project(":androidTests"))
//}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
    linkSqlite = false
}