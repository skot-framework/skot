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


}