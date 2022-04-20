plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {

    jvm("jvm")

    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios {
        binaries {
            framework {
                baseName = "skCore"
            }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}")
            }
        }


        val androidMain by getting {
            dependencies {
                api("com.jakewharton.timber:timber:5.0.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

            }
        }

        val iosMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }

    }

}



android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
    }
    compileSdk = Versions.Android.compileSdk

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
}

if (!localPublication) {

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    val publication = getPublication(project)
    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar.get())

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
}