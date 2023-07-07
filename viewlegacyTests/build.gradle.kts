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
    compileSdk = Versions.Android.compileSdk
    namespace = "tech.skot.viewlegacytests"
}



dependencies {
    implementation((project(":viewlegacy")))
    api("androidx.test.espresso:espresso-core:3.5.1")
    api("androidx.test:core-ktx:1.5.0")
    api("androidx.test.ext:junit-ktx:1.1.5")
}
