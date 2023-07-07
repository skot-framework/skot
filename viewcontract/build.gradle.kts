plugins {
    kotlin("multiplatform")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version


kotlin {
    jvm()

    ios()

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
            }
        }


        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }
    }

}

