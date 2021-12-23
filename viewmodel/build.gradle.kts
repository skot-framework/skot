plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
}

android {
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
    }
    compileSdkVersion(Versions.Android.compileSdk)

    sourceSets {
//        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("test").java.srcDirs("src/javaTest/kotlin")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
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