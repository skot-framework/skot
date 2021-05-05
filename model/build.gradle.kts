plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.squareup.sqldelight")
}

group = Versions.group
version = Versions.version



kotlin {

    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios {
        binaries {
            framework {
                baseName = "sk-model"
            }
        }
    }




    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":modelcontract"))
                api("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-json:${Versions.ktor}")
                api("io.ktor:ktor-client-logging:${Versions.ktor}")
            }
        }


        val androidMain by getting {
            dependencies {
//                api("com.jakewharton.timber:timber:${Versions.Android.timber}")
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


    }


}


android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }
    }
    compileSdkVersion(Versions.Android.compileSdk)

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

//    packagingOptions {
//        exclude("META-INF/*")
//    }


}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    androidTestImplementation(project(":androidTests"))
}

sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
    linkSqlite = false
}