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
                api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                api("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
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