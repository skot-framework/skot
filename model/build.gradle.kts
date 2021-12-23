plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("com.github.ben-manes.versions")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {

    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios {
        binaries {
            framework {
                baseName = "skModel"
            }
        }
    }

//    ios()




    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":modelcontract"))
                api("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-auth:${Versions.ktor}")
//                api("io.ktor:ktor-client-json:${Versions.ktor}")
                api("io.ktor:ktor-client-logging:${Versions.ktor}")
            }
        }


        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}")
            }
        }
//        sourceSets["commonTest"].kotlin.srcDir("src/commonTest/kotlin")
//
//        sourceSets["commonTest"].dependencies {
//            implementation("org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}")
//            implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}")
//        }

        val androidMain by getting {
            dependencies {
                api("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
                api("io.ktor:ktor-client-okhttp:${Versions.ktor}")
//                api("io.ktor:ktor-client-json-jvm:${Versions.ktor}")
//                api("io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")
//                api("io.ktor:ktor-client-logging-jvm:${Versions.ktor}")
//                api("io.ktor:ktor-client-auth-jvm:${Versions.ktor}")
//                api("org.slf4j:slf4j-simple:${Versions.sl4j}")
            }
        }

        val iosMain by getting {
            dependencies {
                api("com.squareup.sqldelight:native-driver:${Versions.sqldelight}")
                api("io.ktor:ktor-client-ios:${Versions.ktor}")
//                api("io.ktor:ktor-client-json-native:${Versions.ktor}")
//                api("io.ktor:ktor-client-serialization-native:${Versions.ktor}")
//                api("io.ktor:ktor-client-logging-native:${Versions.ktor}")
            }
        }


    }


}


android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }
    }
    compileSdkVersion(Versions.Android.compileSdk)

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        if (gradle.startParameter.taskNames.any {
                it.toUpperCase().contains("ANDROIDTEST")
            }) {
            exclude("META-INF/*")
        }
    }


}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlin}")
//    androidTestImplementation(project(":androidTests"))
}

sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
    linkSqlite = false
}

val publication = getPublication(project)
publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set(project.name)
            description.set("${project.name} description")
            url.set("https://github.com/skot-framework/skot")
            licenses {
                license {
                    name.set("Apache 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("MathieuScotet")
                    name.set("Mathieu Scotet")
                    email.set("mscotet.lmit@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/skot-framework/skot.git")
                developerConnection.set("scm:git:ssh://github.com/skot-framework/skot.git")
                url.set("https://github.com/skot-framework/skot/tree/master")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        publication.signingKeyId,
        publication.signingKey,
        publication.signingPassword
    )
    this.sign(publishing.publications)
}