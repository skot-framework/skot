group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
    }
    compileSdk = 33
    namespace = "tech.skot.viewlegacytests"
}



dependencies {
    implementation((project(":viewlegacy")))
    api(libs.espresso.core)
    api(libs.core.ktx)
    api(libs.junit.ktx)
}
