plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {
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
                api(libs.kotlinx.coroutines.core)
                api(kotlin("reflect"))
                api(libs.kotlinx.datetime)
            }
        }


        val jvmMain by getting {
            dependencies {
                api(libs.bundles.kotlinx.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.timber)
                api(libs.bundles.kotlinx.coroutines)

            }
        }


        val jvmTest by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.test.junit)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.espresso.core)
                implementation(libs.core.ktx)
                implementation(libs.junit.ktx)
                implementation(libs.jetbrains.kotlin.test.junit)
            }
        }

    }

}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdk = 33
    namespace = "tech.skot.core"
}
