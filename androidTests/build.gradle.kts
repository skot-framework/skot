group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}


dependencies {

    api(libs.jetbrains.kotlin.stdlib)
    api(libs.kotlinx.coroutines.android)

    api(libs.appcompat)

    api(libs.kotlin.test)

    api(libs.espresso.core)
    api(libs.espresso.contrib)
    api(libs.espresso.web)
    api(libs.test.rules)
    api(libs.core.testing)

    api(libs.ktor.server.netty)
    api(libs.ktor.client.android)

    api(libs.test.runner)
    implementation(project(":core"))
}


android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)

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


    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        api(libs.jetbrains.kotlin.stdlib)
        api(libs.kotlinx.coroutines.core)
    }


    sourceSets["androidMain"].dependencies {
        api(libs.jetbrains.kotlin.stdlib)
        api(libs.kotlinx.coroutines.android)
    }


}