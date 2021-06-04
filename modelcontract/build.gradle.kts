plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

group = Versions.group
version = Versions.version


kotlin {
    jvm("jvm")
    ios {
        binaries {
            framework {
                baseName = "modelContract"
            }
        }
    }
//    sourceSets["jvmMain"].dependencies {
//        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
//    }

    sourceSets {
        val iosMain by getting {
        }
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }
    }


}