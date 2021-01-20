group = Versions.group
version = Versions.version

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

//kotlin {
//    jvm("jvm")
//
//    kotlin {
//        sourceSets["commonMain"].kotlin.srcDir("src/main/kotlin")
//    }
//}