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
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}

//tasks.register("UpdateRepository") {
//    updateRepository()
//}

tasks {
    val skotRepository = "/Users/mscotet/skot/repository"
    val m2LocalRepository = "/Users/mscotet/.m2/repository"

    val prepareTask = create<Exec>("prepareLocalRepository") {
        group = "skot"
        commandLine = listOf("./prepareLocalRepository.sh", skotRepository, m2LocalRepository)
    }

//    val publishToLocalWithM2RepoAsLastPublication = task("publishToLocalWithM2RepoAsLastPublication") {
//        dependsOn(prepareTask)
//    }

    val pushTask = create<Exec>("pushToGitHub") {
//        dependsOn()
        group = "skot"
        commandLine = listOf("./pushToGitHub.sh", skotRepository, m2LocalRepository, Versions.version, rootDir.absolutePath)
    }

//    project.task("skPublishToGitHub") {
//        group = "skot"
//
////        project.exec {
////                commandLine = listOf("./prepareLocalRepository.sh", skotRepository, m2LocalRepository)
////            }
//
//        dependsOn(
//            prepareTask,
//            project.getTasksByName("publishToMavenLocal", true),
//            pushTask
//        )
//
////        doLast {
////            println("--------- after publishToMaven ??")
////        }
////        dependsOn(project.getTasksByName("publishToMavenLocal", true))
////        dependsOn(puhTask)
//
//
////        doLast {
////            project.exec {
////                commandLine = listOf("./pushToGitHub.sh", skotRepository, m2LocalRepository, Versions.version, rootDir.absolutePath)
////            }
////        }
//
//
//    }



}

