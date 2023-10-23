
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
                api(libs.jetbrains.kotlin.stdlib)
                api(libs.kotlinx.coroutines.core)
                api(libs.jetbrains.kotlinx.serialization.core)
                api(libs.kotlinx.datetime)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.test.junit)
            }
        }
    }


}
