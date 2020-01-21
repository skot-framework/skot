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
    api("androidx.appcompat:appcompat:${Versions.appcompat}")
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

    viewBinding {
        isEnabled = true
    }

}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }



    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        implementation(project(":contract"))
    }


    sourceSets["androidMain"].dependencies {
    }



}