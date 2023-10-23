@file:Suppress("UnstableApiUsage")

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
                api(project(":core"))
                api(project(":viewcontract"))
                api(project(":modelcontract"))
            }
        }
    }

}

dependencies {
    testImplementation(libs.jetbrains.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
    }
    compileSdk = 33
    namespace = "tech.skot.viewmodel"




    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
    }

}

