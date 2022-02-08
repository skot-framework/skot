import org.gradle.api.Project

var localPublication = true

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
