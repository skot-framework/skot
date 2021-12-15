package tech.skot.tools.starter

import com.squareup.kotlinpoet.ClassName
import tech.skot.tools.generation.writeLinesTo
import java.nio.file.Files
import java.nio.file.Path


class ModuleGenerator(
    val name: String,
    val configuration: StarterConfiguration,
    val rootDir: Path
) {


    companion object {
        const val activityTemplateSimple = """        <activity
            android:name="%s"
            android:allowTaskReparenting="true"
            android:screenOrientation="portrait"
            android:theme="@style/%s"/>
        """

        const val activityTemplateRoot = """        <activity
            android:name="%s"
            android:allowTaskReparenting="true"
            android:exported="true"
            android:launchMode="singleTask"
            android:screenOrientation="portrait"
            android:theme="@style/%s">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <!--            <intent-filter>-->
<!--                <action android:name="android.intent.action.VIEW" />-->

<!--                <category android:name="android.intent.category.DEFAULT" />-->
<!--                <category android:name="android.intent.category.BROWSABLE" />-->
<!--                -->
<!--                <data-->
<!--                    android:host="my_host"-->
<!--                    android:pathPattern="/my/path/*/pattern"-->
<!--                    android:scheme="https" />-->
<!--            </intent-filter>-->
        </activity>"""
    }


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

    var androidAppTheme: String? = null
    fun androidAppTheme(appTheme: String) {
        androidAppTheme = appTheme
    }

    var appName: String? = null

    data class Activity(val className: ClassName, val template: String, val theme: String)

    var androidActivities: List<Activity>? = null

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

        val main = if (justAndroid) "androidMain" else "commonMain"

        mainPackage?.let { buildDir("$name/src/$main/kotlin/${it.replace(".", "/")}") }





        androidPackage?.let {
            println("------generate manifest")

            val applicationOpenTag: List<String> = when {
                androidApplicationClass != null -> {
                    listOf("\t<application", "\t\tandroid:name=\".$androidApplicationClass\"") +
                            (androidAppTheme?.let { listOf("\t\tandroid:theme=\"@style/$it\"") }
                                ?: emptyList<String>()) +
                            (appName?.let { listOf("\t\tandroid:label=\"$it\"") }
                                ?: emptyList<String>()) +
                            "\t\t>"
                }
                !androidActivities.isNullOrEmpty() -> {
                    listOf("\t<application>")
                }
                else -> emptyList<String>()
            }
            val applicationCloseTag = when {
                androidApplicationClass != null || !androidActivities.isNullOrEmpty() -> {
                    listOf("\t</application>")
                }
                else -> emptyList<String>()
            }


            rootDir.writeLinesTo("$name/src/androidMain/AndroidManifest.xml", listOfNotNull(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"",
                "\tpackage=\"$it\">"
            ) +
                    androidPermissions.map { "\t<uses-permission android:name=\"android.permission.$it\" />" } +
                    applicationOpenTag +
                    (androidActivities?.flatMap { activity ->
                        activity.template.format(
                            activity.className.canonicalName,
                            activity.theme
                        ).split("\n")
                    } ?: emptyList<String>()) +
                    applicationCloseTag +
                    "</manifest>"
            )
        }

        if (androidStrings.isNotEmpty()) {
            println("------ generate strings")
            rootDir.writeLinesTo("$name/src/androidMain/res/values/strings.xml",
                listOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<resources>")
                        + androidStrings.map { "\t<string name=\"${it.key}\">\"${it.value}\"</string>" }
                        + "</resources>"
            )
        }
    }

}