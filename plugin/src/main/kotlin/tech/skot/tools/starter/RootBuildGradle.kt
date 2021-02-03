package tech.skot.tools.starter

val rootBuildGradle = """buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath("tech.skot:plugin:${'$'}{Versions.skot}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
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