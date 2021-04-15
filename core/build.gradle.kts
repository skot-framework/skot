plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
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
                baseName = "core"
            }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}")
                //api("com.soywiz.korlibs.klock:klock:${Versions.klock}")
            }
        }


        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:${Versions.Android.timber}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

            }
        }

        val iosMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}") {
                    version {
                        strictly(Versions.kotlinCoroutines)
                    }
                }
            }
        }
    }

}



android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)

    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}