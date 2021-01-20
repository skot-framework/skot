group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}


dependencies {

    api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")

    api("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")

    api("androidx.test.espresso:espresso-core:3.3.0")
    api("androidx.test.espresso:espresso-contrib:3.3.0")
    api("androidx.test.espresso:espresso-web:3.3.0")
    api("androidx.test:rules:1.3.0")
    api("androidx.arch.core:core-testing:2.1.0")

    api("io.ktor:ktor-server-netty:${Versions.ktor}")
    api("io.ktor:ktor-client-android:${Versions.ktor}")

    api("androidx.test:runner:1.3.0")
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
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
    }


    sourceSets["androidMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    }


}