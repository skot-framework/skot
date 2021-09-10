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

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "maven-publish")

}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}


tasks {
    val skotRepository = "/Users/mscotet/skot/repository"
    val m2LocalRepository = "/Users/mscotet/.m2/repository"


    create<Exec>("publishToGitHub") {
        group = "skot"
        commandLine = listOf("./publishToGitHub.sh", skotRepository, m2LocalRepository, Versions.version, rootDir.absolutePath)
    }


}

