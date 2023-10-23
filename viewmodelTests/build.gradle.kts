plugins {
    id("java-library")
    kotlin("multiplatform")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {
    jvm("jvm")


    sourceSets {
        val jvmMain by getting {

            kotlin.srcDir("src/main/kotlin")

            dependencies {
                api(libs.jetbrains.kotlin.stdlib)
                api(libs.jetbrains.kotlin.test.junit)
                api(libs.kotlinx.coroutines.test)
                api(project(":core"))
                implementation(project(":viewmodel"))
                implementation(project(":viewcontract"))
                implementation(project(":modelcontract"))
            }
        }

    }
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}