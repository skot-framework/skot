package tech.skot.tools.starter.view

import com.squareup.kotlinpoet.*
import tech.skot.Versions
import tech.skot.tools.generation.writeStringTo
import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.view() {
    ModuleGenerator("view", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("tech.skot.viewlegacy"))
            plugin(BuildGradleGenerator.Plugin.Kotlin("android"))

        }
        androidPackage = configuration.appPackage + ".view"
        justAndroid = true
        mainPackage = configuration.appPackage


        val baseActivityClassName = ClassName(configuration.appPackage + ".android", "BaseActivity")
        val splashActivityClassName =
            ClassName(configuration.appPackage + ".android", "SplashActivity")

        androidActivities = listOf(
            ModuleGenerator.Activity(
                className = splashActivityClassName,
                template = ModuleGenerator.activityTemplateRoot,
                theme = "SplashTheme"
            ),
            ModuleGenerator.Activity(
                className = baseActivityClassName,
                template = ModuleGenerator.activityTemplateSimple,
                theme = "AppTheme"
            )
        )

        FileSpec.builder(baseActivityClassName.packageName, baseActivityClassName.simpleName)
            .addType(
                TypeSpec.classBuilder(baseActivityClassName.simpleName)
                    .superclass(ClassName("tech.skot.core.components", "SKActivity"))
                    .addModifiers(KModifier.OPEN)
                    .addProperty(
                        PropertySpec.builder(
                            "featureInitializer",
                            ClassName(
                                configuration.appPackage,
                                "${
                                    configuration.appPackage.substringAfterLast(".").capitalize()
                                }Initializer"
                            )
                        )
                            .initializer("get()")
                            .addModifiers(KModifier.OVERRIDE)
                            .build()
                    )
                    .build()
            )
            .addImport("tech.skot.core.di", "get")
            .build()
            .writeTo(rootDir.resolve("$name/src/androidMain/kotlin"))


        FileSpec.builder(splashActivityClassName.packageName, splashActivityClassName.simpleName)
            .addType(
                TypeSpec.classBuilder(splashActivityClassName.simpleName)
                    .superclass(baseActivityClassName)
                    .addFunction(
                        FunSpec.builder("onNewIntent")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter(
                                "intent",
                                ClassName("android.content", "Intent").copy(true)
                            )
                            .addStatement("super.onNewIntent(intent)")
                            .addStatement("intent?.data?.toSKUri()?.let { featureInitializer.onDeepLink?.invoke(it) }")
                            .build()
                    )
                    .build()
            )
            .addImport("tech.skot.core", "toSKUri")
            .build()
            .writeTo(rootDir.resolve("$name/src/androidMain/kotlin"))


        val initializeView = ClassName("${configuration.appPackage}.di", "initializeView")
            FileSpec.builder(initializeView.packageName, initializeView.simpleName)
                .addImport("tech.skot.core.components", "SKComponentView")
                .addImport("android.view", "Gravity")
                .addImport("android.widget", "FrameLayout")
                .addImport("com.google.android.material.snackbar", "Snackbar")
                .addImport("android.os", "Build")
                .addImport("tech.skot.core.components", "SKActivity")
                .addImport(splashActivityClassName.packageName, splashActivityClassName.simpleName)
                .addFunction(
                    FunSpec.builder(initializeView.simpleName)
                        .addModifiers(KModifier.SUSPEND)
                        .addStatement("SKActivity.launchActivityClass = SplashActivity::class.java")
                        .addCode(
                            CodeBlock.of(
                                """SKComponentView.displayError = { message ->
        Snackbar.make(activity.window.decorView, message, Snackbar.LENGTH_LONG)
            .apply {
                view.apply {
                    (layoutParams as? FrameLayout.LayoutParams)?.let {
                        it.gravity = Gravity.TOP
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            it.topMargin = activity.window?.decorView?.rootWindowInsets?.systemWindowInsetTop
                                ?: 0
                        }

                        layoutParams = it
                    }
                }
                show()
            }
    }"""
                            )
                        )
                        .build()
                )
                .build()
                .writeTo(rootDir.resolve("$name/src/androidMain/kotlin"))




        rootDir.writeStringTo(
            "$name/src/androidMain/res/values/style.xml", """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="windowNoTitle">true</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
    </style>
    <style name="SplashTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!--Ici ajouter un background qui sera affiché dès le lancement de l'app, avant le calcul du premier écran-->
        <!--item name="android:windowBackground">@drawable/background_splash</item-->
    </style>
</resources>            
""".trimIndent()
        )

        rootDir.writeStringTo(
            "$name/src/androidMain/res/layout/splash.xml", """<?xml version="1.0" encoding="utf-8"?>
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


        rootDir.writeStringTo(
            "$name/src/androidMain/res_referenced/values/strings.xml",
            """<?xml version="1.0" encoding="utf-8"?>
<resources>
</resources>"""
        )
    }.generate()
    modules.add("view")
}