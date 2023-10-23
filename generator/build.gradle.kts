plugins {
    id("java-library")
    kotlin("multiplatform")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version


//configurations {
//    all {
//        attributes {
//            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
//        }
//    }
//
//    sourceSets {
//        getByName("main").java.srcDirs("src/jvmMain/kotlin")
//    }
//}

kotlin {
    jvm("jvm")


    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlin.stdlib)
                api(project(":viewcontract"))
                implementation(project(":modelcontract"))
                api(libs.kotlinpoet)
                api(kotlin("reflect"))
                implementation(libs.jetbrains.kotlin.compiler.embeddable)
            }
        }
    }
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}