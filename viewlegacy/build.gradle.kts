
group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}


dependencies {
    api("androidx.core:core:1.10.1")
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.viewpager2:viewpager2:1.0.0")
    api("androidx.recyclerview:recyclerview:1.3.0")
    api("com.google.android.material:material:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    androidTestImplementation(project(":viewlegacyTests"))
}


android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdk = Versions.Android.compileSdk
    namespace = "tech.skot.viewlegacy"


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


    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        api(project(":viewcontract"))
    }


    sourceSets["androidMain"].dependencies {
    }

//    sourceSets["androidInstrumentedTest"].resources.srcDir("src/androidInstrumentedTest/res")

    println("-----@@@@@@@---- ${sourceSets.asMap}")

}
