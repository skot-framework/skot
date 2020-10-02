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
                baseName = "sk-contract"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))

            }
        }

    }

//    sourceSets["commonMain"].dependencies {
//        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
//    }
//
//    sourceSets["jvmMain"].dependencies {
//
//    }

//    sourceSets["iosMain"].dependencies {
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
//    }
}
