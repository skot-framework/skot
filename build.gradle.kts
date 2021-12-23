buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.Android.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}")
        classpath("com.github.ben-manes:gradle-versions-plugin:+")

        classpath("com.squareup:kotlinpoet:${Versions.kotlinpoet}")

    }
}
group=Versions.group
version=Versions.version

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

//apply(plugin = "io.github.gradle-nexus.publish-plugin")

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}


allprojects {
    repositories {
        google()
        mavenCentral()
    }
//    apply("com.github.ben-manes.versions")

//    apply(plugin = "maven-publish")
//    apply(plugin = "org.gradle.signing")

}

val publication = getPublication(project)

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(publication.sonatypeStagingProfileId)
            username.set(publication.ossrhUsername)
            password.set(publication.ossrhPassword)
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
