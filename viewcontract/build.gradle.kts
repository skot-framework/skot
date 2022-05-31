plugins {
    kotlin("multiplatform")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version


kotlin {
    jvm("jvm")

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
            }
        }


        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }
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

