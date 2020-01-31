group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}


dependencies {
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
    api("androidx.constraintlayout:constraintlayout:${Versions.Android.constraintLayout}")
    api("androidx.viewpager2:viewpager2:${Versions.Android.viewpager2}")
    api("androidx.recyclerview:recyclerview:${Versions.Android.recyclerview}")
}


android {
    defaultConfig {
        minSdkVersion(Android.minSdk)
    }
    compileSdkVersion(Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").java.srcDirs("generated/androidMain/kotlin")
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


    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        implementation(project(":contract"))
    }


    sourceSets["androidMain"].dependencies {
    }


}