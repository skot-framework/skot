plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {

    jvm()

    android()

    ios {
        binaries {
            framework {
                baseName = "skViewModel"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":viewcontract"))
                api(project(":modelcontract"))
            }
        }
    }

}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
    }
    compileSdk = Versions.Android.compileSdk

    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
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

