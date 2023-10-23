buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlin.serialization)
        classpath(libs.gradle.plugin)
        classpath(libs.kotlinpoet)

    }
}
group=Versions.group
version=Versions.version

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("com.github.ben-manes.versions") version "0.42.0"
}


tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.let {
            it.contains("alpha") || it.contains("beta") || it.contains("-rc")
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
