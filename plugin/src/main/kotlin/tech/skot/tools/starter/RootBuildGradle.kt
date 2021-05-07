package tech.skot.tools.starter

val rootBuildGradle = """buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("http://raw.github.com/MathieuScotet/skot/repository")
        }
    }

    dependencies {
        classpath("tech.skot:plugin:${'$'}{Versions.skot}")
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("http://raw.github.com/MathieuScotet/skot/repository")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }

}"""