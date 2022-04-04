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
        classpath("com.squareup:kotlinpoet:${Versions.kotlinpoet}")

    }
}
group=Versions.group
version=Versions.version

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("com.github.ben-manes.versions") version "0.39.0"
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

if (!localPublication) {
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
}




