group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    jvm("jvm")

    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64

//    iOSTarget("ios") {
////        binaries {
////            framework {
////                baseName = "OufHP"
////            }
////        }
//    }

    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
    }

    sourceSets["jvmMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    }

//    sourceSets["iosMain"].dependencies {
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
//    }
}
