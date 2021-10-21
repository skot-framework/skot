plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = Versions.group
version = Versions.version



kotlin {

    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios {
        binaries {
            framework {
                baseName = "skViewModel"
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
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
}

android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)

    sourceSets {
//        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
