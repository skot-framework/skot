group = Versions.group
version = Versions.version

plugins {
//    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("com.github.ben-manes.versions")
}


android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)
    sourceSets.getByName("main") {
        java.srcDir("src/main/kotlin")
    }


//    publishLibraryVariants("release", "debug")
//    publishLibraryVariantsGroupedByFlavor = true
//    sourceSets {
//        getByName("main").java.srcDirs("src/androidMain/kotlin")
//        getByName("main").java.srcDirs("generated/androidMain/kotlin")
//        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion  = Versions.Android.compose
        kotlinCompilerVersion  = "1.4.10"
    }
//    packagingOptions {
//        exclude("META-INF/*.kotlin_module")
//        exclude("META-INF/*")
//    }
}


dependencies {
    api(project(":core"))
    api("androidx.appcompat:appcompat:${Versions.Android.appcompat}")
    api("androidx.compose.ui:ui:${Versions.Android.compose}")
    api("androidx.compose.material:material:${Versions.Android.compose}")
    api("androidx.ui:ui-tooling:${Versions.Android.compose}")
}

//kotlin {
//
//    android("android") {
////        val main by compilations.getting {
////            kotlinOptions {
////                // Setup the Kotlin compiler options for the 'main' compilation:
////                jvmTarget = "1.8"
////            }
////
////
////        }
//
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = JavaVersion.VERSION_1_8.toString()
//                useIR = true
//            }
//        }
//
//        publishLibraryVariants("release", "debug")
//        publishLibraryVariantsGroupedByFlavor = true
//
////        val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
////
////        compileKotlin.kotlinOptions {
////            jvmTarget = "1.8"
////            useIR = true
////        }
//    }
//
////    targets.all {
////        compilations.all {
////            kotlinOptions {
////                jvmTarget = "1.8"
////                useIR = true
////            }
////        }
////    }
//
//}


//kotlin {
//    android("android") {
//        publishLibraryVariants("release", "debug")
//        publishLibraryVariantsGroupedByFlavor = true
//    }
//
////    //select iOS target platform depending on the Xcode environment variables
////    val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
////            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
////                ::iosArm64
////            else
////                ::iosX64
////
////    iOSTarget("ios") {
//////        binaries {
//////            framework {
//////                baseName = "OufHP"
//////            }
//////        }
////    }
//
//    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
//    sourceSets["commonMain"].dependencies {
//        implementation(project(":core"))
//    }
//
//
//    sourceSets["androidMain"].dependencies {
//    }
//
//
//}