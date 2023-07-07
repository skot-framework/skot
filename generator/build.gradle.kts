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
    jvmToolchain(8)


    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                api(project(":viewcontract"))
                implementation(project(":modelcontract"))
                api("com.squareup:kotlinpoet:${Versions.kotlinpoet}")
                api(kotlin("reflect"))
                implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}")
            }
        }
    }
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}