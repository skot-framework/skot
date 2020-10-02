plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}

group = Versions.group
version = Versions.version


//repositories {
//    google()
//    jcenter()
//    mavenCentral()
//}





kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

//    android()

    ios {
        binaries {
            framework {
                baseName = "sk-core"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))

            }
        }

        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:${Versions.Android.timber}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

            }
        }

        val iosTest by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.kotlinCoroutines}")
            }
        }

    }

}



android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
//        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}