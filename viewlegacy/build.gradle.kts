
group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}


dependencies {
    api(libs.core)
    api(libs.appcompat)
    api(libs.constraintlayout)
    api(libs.viewpager2)
    api(libs.recyclerview)
    api(libs.material)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.lifecycle.runtime.ktx)
    androidTestImplementation(project(":viewlegacyTests"))
}


android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdk = 33
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
