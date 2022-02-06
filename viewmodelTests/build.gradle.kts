plugins {
    id("java-library")
    kotlin("multiplatform")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {
    jvm("jvm")

    sourceSets {
        val jvmMain by getting {

            kotlin.srcDir("src/main/kotlin")

            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                api("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
                api(project(":core"))
                implementation(project(":viewmodel"))
                implementation(project(":viewcontract"))
                implementation(project(":modelcontract"))
            }
        }

        val jvmTest by getting {
            kotlin.srcDir("src/test/kotlin")
        }
    }
}


if (!localPublication) {
    val publication = getPublication(project)

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
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
