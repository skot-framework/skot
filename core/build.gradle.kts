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

    ios {
        binaries {
            framework {
                baseName = "sk-core"
            }
        }
    }


//        val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
//            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
//                ::iosArm64
//            else
//                ::iosX64
//
//    iOSTarget("ios") {
//        binaries {
//            framework {
//                baseName = "sk-core"
//            }
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))
                api("com.soywiz.korlibs.klock:klock:${Versions.klock}")
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
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
//        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}