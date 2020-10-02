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

    android("android") {
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
                implementation(project(":contract"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:${Versions.Android.timber}")
                implementation("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:${Versions.sqldelight}")
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
}