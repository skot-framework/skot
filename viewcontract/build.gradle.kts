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
    sourceSets["jvmMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    }

    sourceSets {
        val iosMain by getting {
        }
    }


}