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
                baseName = "core"
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
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }
    }


}