group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
}

android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
    }
    compileSdk = Versions.Android.compileSdk

    buildFeatures {
        viewBinding = true
    }
}



dependencies {
    implementation((project(":viewlegacy")))
    api("androidx.test.espresso:espresso-core:3.4.0")
    api("androidx.test:core-ktx:1.4.0")
    api("androidx.test.ext:junit-ktx:1.1.3")
}


if (!localPublication) {
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
}