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
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
                api(project(":viewcontract"))
                implementation(project(":modelcontract"))
                api("com.squareup:kotlinpoet:${Versions.kotlinpoet}")
                api(kotlin("reflect"))
                implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}")
            }
        }

//        val jvmTest by getting {
//            dependencies {
//                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
//            }
//        }
    }
}

if (!localPublication) {
    val publication = getPublication(project)

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }
//    val sourceJar by tasks.registering(Jar::class) {
//        archiveClassifier.set("sources")
//        from(sourceSets.main.get().allSource)
//    }

    publishing {
        publications {
            publications.withType<MavenPublication> {
//            create<MavenPublication>("maven") {
//                from(components["java"])
                artifact(javadocJar.get())
//                artifact(sourceJar.get())

                pom {
                    name.set("Skot Framework "+project.name)
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

