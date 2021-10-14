package tech.skot.tools.starter

import tech.skot.Versions

val rootBuildGradle = """buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        classpath("${Versions.group}:plugin:${'$'}{Versions.skot}")
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

}"""