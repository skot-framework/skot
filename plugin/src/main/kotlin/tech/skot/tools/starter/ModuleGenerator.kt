package tech.skot.tools.starter

import tech.skot.tools.generation.writeLinesTo
import tech.skot.tools.generation.writeStringTo
import java.nio.file.Files
import java.nio.file.Path


const val skActivityManifestDeclaration = """    <application>
        <activity
            android:name="tech.skot.core.components.SKActivity"
            android:allowTaskReparenting="true"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustPan">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>"""

class ModuleGenerator(val name:String, val configuration: StarterConfiguration, val rootDir: Path) {



    private val buildGradle = BuildGradleGenerator()
    fun buildGradle(block:BuildGradleGenerator.()->Unit) {
        buildGradle.block()
    }

    var mainPackage:String? = null

    fun buildDir(relativePath:String) {
        Files.createDirectories(rootDir.resolve(relativePath))
    }

    var androidPackage:String? = null
    fun android(packageName:String) {
        androidPackage = packageName
    }

    var androidSKActivity:Boolean = false

    var androidStrings = mutableMapOf<String,String>()
    fun androidString(key:String, value:String) {
        androidStrings[key] = value
    }

    var androidPermissions = mutableListOf<String>()
    fun androidPermission(permission:String) {
        androidPermissions.add(permission)
    }

    fun generate() {

        rootDir.writeLinesTo("$name/build.gradle.kts", buildGradle.generate())

        mainPackage?.let { buildDir("$name/src/commonMain/kotlin/${it.replace(".","/")}") }

        androidPackage?.let {
            rootDir.writeLinesTo("$name/src/androidMain/AndroidManifest.xml", listOfNotNull(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
                    "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"",
                    "\tpackage=\"$it\">") +
                    androidPermissions.map { "\t<uses-permission android:name=\"$it\" />" } +
                    (if (androidSKActivity) skActivityManifestDeclaration.split("\n") else emptyList<String>()) +
                    "</manifest>"
            )
        }

        if (androidStrings.isNotEmpty()){
            rootDir.writeLinesTo("$name/src/androidMain/res/values/strings.xml",
                    listOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<resources>")
                            + androidStrings.map {  "\t<string name=\"${it.key}\">\"${it.value}\"</string>" }
            + "</resources>"
            )
        }
    }

}