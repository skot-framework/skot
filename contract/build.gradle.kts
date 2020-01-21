group = "tech.skot"
version = "0.1"

repositories {
    google()
    jcenter()
    mavenCentral()
}


plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    jvm("jvm")

    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
    }

    sourceSets["jvmMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    }
}
