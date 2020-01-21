import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

group = "tech.skot"
version = "0.1"

repositories {
    google()
    jcenter()
    mavenCentral()
}


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}



dependencies {
    api("com.jakewharton.timber:timber:${Versions.timber}")
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }



    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutines}")
        api(kotlin("reflect"))

    }


    sourceSets["androidMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    }

}
