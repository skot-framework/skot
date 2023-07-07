plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {
    jvmToolchain(8)
    jvm()

    ios()

    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}")
            }
        }


        val jvmMain by getting {

        }

        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:5.0.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

            }
        }


        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.test.espresso:espresso-core:3.5.1")
                implementation("androidx.test:core-ktx:1.5.0")
                implementation("androidx.test.ext:junit-ktx:1.1.5")
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }

    }

}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdk = Versions.Android.compileSdk
    namespace = "tech.skot.core"
}
