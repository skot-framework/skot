group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    jvm("jvm")


    ios {
        binaries {
            framework {
                baseName = "contract"
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")
    }

    sourceSets["jvmMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    }


}
