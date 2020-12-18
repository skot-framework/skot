group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("com.github.ben-manes.versions")
}


dependencies {
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
    api("androidx.constraintlayout:constraintlayout:${Versions.Android.constraintLayout}")
    api("androidx.viewpager2:viewpager2:${Versions.Android.viewpager2}")
    api("androidx.recyclerview:recyclerview:${Versions.Android.recyclerview}")
    api("com.google.android.material:material:${Versions.Android.material}")

}


android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)

    sourceSets {
        getByName("main") {
            java.srcDirs("src/androidMain/kotlin")
            java.srcDirs("generated/androidMain/kotlin")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }

    buildFeatures {
        viewBinding = true
    }

//    packagingOptions {
//        exclude("META-INF/*.kotlin_module")
//        exclude("META-INF/*")
//    }
}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }


    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
    }


    sourceSets["androidMain"].dependencies {
    }


}