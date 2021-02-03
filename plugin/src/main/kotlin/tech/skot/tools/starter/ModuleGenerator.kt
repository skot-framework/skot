package tech.skot.tools.starter

import tech.skot.tools.generation.writeLinesTo
import java.nio.file.Files
import java.nio.file.Path


const val skActivityManifestDeclaration = """        <activity
            android:name="tech.skot.core.components.SKActivity"
            android:allowTaskReparenting="true"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>"""

class ModuleGenerator(val name: String, val configuration: StarterConfiguration, val rootDir: Path) {


    private val buildGradle = BuildGradleGenerator()
    fun buildGradle(block: BuildGradleGenerator.() -> Unit) {
        buildGradle.block()
    }

    var mainPackage: String? = null

    var justAndroid: Boolean = false
    var androidApplicationClass: String? = null

    fun buildDir(relativePath: String) {
        Files.createDirectories(rootDir.resolve(relativePath))
    }

    var androidPackage: String? = null
    fun android(packageName: String) {
        androidPackage = packageName
    }

    var androidSKActivity: Boolean = false

    var androidStrings = mutableMapOf<String, String>()
    fun androidString(key: String, value: String) {
        androidStrings[key] = value
    }

    var androidPermissions = mutableListOf<String>()
    fun androidPermission(permission: String) {
        androidPermissions.add(permission)
    }

    fun generate() {

        rootDir.writeLinesTo("$name/build.gradle.kts", buildGradle.generate())

        val main = if (justAndroid) "main" else "commonMain"

        mainPackage?.let { buildDir("$name/src/$main/kotlin/${it.replace(".", "/")}") }





        androidPackage?.let {
            val applicationOpenTag = when {
                androidApplicationClass != null -> {
                    listOf("\t<application","\t\tandroid:name=\".$androidApplicationClass\"","\t\t>")
                }
                androidSKActivity -> {
                    listOf("\t<application>")
                }
                else -> emptyList<String>()
            }
            val applicationCloseTag = when {
                androidApplicationClass != null || androidSKActivity -> {
                    listOf("\t</application>")
                }
                else -> emptyList<String>()
            }

            rootDir.writeLinesTo("$name/src/${if (justAndroid) "main" else "androidMain"}/AndroidManifest.xml", listOfNotNull(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
                    "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"",
                    "\tpackage=\"$it\">") +
                    androidPermissions.map { "\t<uses-permission android:name=\"$it\" />" } +
                    applicationOpenTag +
                    (if (androidSKActivity) skActivityManifestDeclaration.split("\n") else emptyList<String>()) +
                    applicationCloseTag +
                    "</manifest>"
            )
        }

        if (androidStrings.isNotEmpty()) {
            rootDir.writeLinesTo("$name/src/androidMain/res/values/strings.xml",
                    listOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<resources>")
                            + androidStrings.map { "\t<string name=\"${it.key}\">\"${it.value}\"</string>" }
                            + "</resources>"
            )
        }
    }

}