import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure


data class Publication(
    val signingKeyId: String,
    val signingPassword: String,
    val signingKey: String,
    val ossrhUsername: String,
    val ossrhPassword: String,
    val sonatypeStagingProfileId: String
)

var publicationProperties: Publication? = null

fun getPublication(project: Project): Publication {
    return publicationProperties
        ?: buildPublicationProperties(project).also { publicationProperties = it }
}

fun buildPublicationProperties(project: Project): Publication {
    val localPropertiesFile = project.rootProject.file("local.properties")
    val localProperties = if (localPropertiesFile.exists()) {
        val props = java.util.Properties()
        props.load(java.io.FileInputStream(localPropertiesFile))
        props
    } else {
        null
    }
    return Publication(
        signingKeyId = localProperties?.getProperty("mavencentral.signingkeyId")
            ?: System.getenv("SIGNING_KEY_ID"),
        signingPassword = localProperties?.getProperty("mavencentral.signingpassword")
            ?: System.getenv("SIGNING_PASSWORD"),
        signingKey = localProperties?.getProperty("mavencentral.signingkey")
            ?: System.getenv("SIGNING_KEY"),
        ossrhUsername = localProperties?.getProperty("mavencentral.ossrhUsername") ?: System.getenv(
            "OSSRH_USERNAME"
        ),
        ossrhPassword = localProperties?.getProperty("mavencentral.ossrhPassword") ?: System.getenv(
            "OSSRH_PASSWORD"
        ),
        sonatypeStagingProfileId = localProperties?.getProperty("mavencentral.sonatypeStagingProfileId")
            ?: System.getenv("SONATYPE_STAGING_PROFILE_ID"),
    )
}

//fun Project.configurePublication(artefactId: String, artefactDescription: String) {
//    configure<PublishingExtension> {
//        publications.withType<MavenPublication> {
//            pom {
//                name.set("viewcontract")
//                description.set("ViewContract ")
//                url.set("https://github.com/skot-framework/skot")
//                licenses {
//                    license {
//                        name.set("Apache 2.0")
//                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("MathieuScotet")
//                        name.set("Mathieu Scotet")
//                        email.set("mscotet.lmit@gmail.com")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:github.com/skot-framework/skot.git")
//                    developerConnection.set("scm:git:ssh://github.com/skot-framework/skot.git")
//                    url.set("https://github.com/skot-framework/skot/tree/master")
//                }
//            }
//        }
//    }
//}


//fun Project.configurePublication(artefactId:String, artefactDescription:String) {
//    val publication = getPublication(project)
//    publishing {
//        publications.withType<MavenPublication> {
//            pom {
//                name.set("viewcontract")
//                description.set("ViewContract ")
//                url.set("https://github.com/skot-framework/skot")
//                licenses {
//                    license {
//                        name.set("Apache 2.0")
//                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("MathieuScotet")
//                        name.set("Mathieu Scotet")
//                        email.set("mscotet.lmit@gmail.com")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:github.com/skot-framework/skot.git")
//                    developerConnection.set("scm:git:ssh://github.com/skot-framework/skot.git")
//                    url.set("https://github.com/skot-framework/skot/tree/master")
//                }
//            }
//        }
//    }
//
//    signing {
//        useInMemoryPgpKeys(
//            publication.signingKeyId,
//            publication.signingKey,
//            publication.signingPassword
//        )
//        this.sign(publishing.publications)
//    }
//}