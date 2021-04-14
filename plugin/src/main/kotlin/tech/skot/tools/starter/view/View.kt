package tech.skot.tools.starter.view

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.Versions
import tech.skot.tools.generation.writeStringTo
import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.view() {
    ModuleGenerator("view", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("skot-viewlegacy"))
            plugin(BuildGradleGenerator.Plugin.Kotlin("android"))

        }
        androidPackage = configuration.appPackage + ".view"
        justAndroid = true
        mainPackage = configuration.appPackage


        val baseActivityClass = ClassName(configuration.appPackage + ".android", "BaseActivity")
        androidBaseActivity = baseActivityClass

        FileSpec.builder(baseActivityClass.packageName, baseActivityClass.simpleName)
                .addType(
                        TypeSpec.classBuilder(baseActivityClass.simpleName)
                                .superclass(ClassName("tech.skot.core.components", "SKActivity"))
                                .addModifiers(KModifier.OPEN)
                                .build()
                )
                .build()
                .writeTo(rootDir.resolve("$name/src/androidMain/kotlin"))

        rootDir.writeStringTo("$name/src/androidMain/res/values/style.xml", """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="BaseAppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="windowNoTitle">true</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
    </style>
</resources>            
""".trimIndent()
        )

        rootDir.writeStringTo("$name/src/androidMain/res/layout/splash.xml", """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="24dp"
        android:textStyle="bold"
        android:text="${configuration.appName}" />
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="24dp"
        android:text="Hello !" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        android:text="Skot starter v${Versions.skot}" />

</LinearLayout>           
""".trimIndent()
        )


        rootDir.writeStringTo("$name/src/androidMain/res_referenced/values/strings.xml","""<?xml version="1.0" encoding="utf-8"?>
<resources>
</resources>""")
    }.generate()
    modules.add("view")
}