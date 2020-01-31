group = Versions.group
version = Versions.version


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("com.squareup.sqldelight")
}



dependencies {
    implementation("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
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
        api(project(":core"))
        implementation(project(":contract"))
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${Versions.serialization}")
    }


    sourceSets["androidMain"].dependencies {
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}")
    }

}

sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
}