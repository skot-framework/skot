package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*
import java.nio.file.Files

fun Generator.generateViewLegacy() {

    println("generate ViewLegacy .....")
    val viewModuleAndroidPackage =
        getAndroidPackageName(rootPath.resolve(modules.view).resolve("src/androidMain"))

    if (!baseActivity.existsAndroidInModule(modules.view)) {
        baseActivity.fileClassBuilder(
            listOf(FrameworkClassNames.get)
        ) {
            superclass(FrameworkClassNames.skActivity)
            addModifiers(KModifier.OPEN)
            addProperty(
                PropertySpec.builder("featureInitializer", appFeatureInitializer)
                    .initializer("get()")
                    .addModifiers(KModifier.OVERRIDE)
                    .build()
            )
        }.writeTo(androidSources(modules.view))
    }

    val rootActivity = ClassName(packageName = baseActivity.packageName, "RootActivity")
    if (!rootActivity.existsAndroidInModule(modules.view)) {
        rootActivity.fileClassBuilder(
            listOf(FrameworkClassNames.toSKUri)
        ) {
            superclass(baseActivity)
            addFunction(FunSpec.builder("onNewIntent")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("intent", AndroidClassNames.intent.nullable())
                .addStatement("super.onNewIntent(intent)")
                .addStatement("intent?.data?.toSKUri()?.let { featureInitializer.onDeepLink?.invoke(it, false) }")
                .build())
        }.writeTo(androidSources(modules.view))
    }

    components.forEach {
        val layoutPath = androidResLayoutPath(modules.view, it.layoutName())


        if (!it.proxy().existsAndroidInModule(modules.view)){


            FileSpec.builder(
                it.proxy().packageName,
                it.proxy().simpleName
            )
                .addType(it.buildProxy(this, viewModuleAndroidPackage, baseActivity))
                .addType(it.buildRAI(viewModuleAndroidPackage))
                .apply {
                    if (it.hasLayout) {
                        addImportClassName(viewR)
                    }
                    it.subComponents.filter { it.passToParentView }
                        .forEach {
                            addImportClassName(it.viewImplClassName)
                        }
                }.build()
                .writeTo(generatedAndroidSources(modules.view))
        }

        if (!it.viewImpl().existsAndroidInModule(modules.view)) {
            FileSpec.builder(
                it.viewImpl().packageName,
                it.viewImpl().simpleName
            )
                .addType(it.buildViewImpl(viewModuleAndroidPackage))
                .apply {
                    it.interfacesImpl.forEach {
                        addImportClassName(it)
                    }
                }
                .build()
                .writeTo(androidSources(modules.view))
        }
        if (!existsPath(layoutPath, "res")) {
            Files.createDirectories(layoutPath.parent)
            layoutPath.toFile().writeText(LAYOUT_TEMPLATE.format(it.name.uppercase()))
        }

    }

    generateViewLegacyInjectorImpl(modules.view)
    generateAndroidTestAbstractTestView()
}

const val LAYOUT_TEMPLATE = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="%s"
        android:textSize="24dp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
"""


fun String.toProxy() = when {
    endsWith("VC") -> {
        substring(0, indexOf("VC")).suffix("ViewProxy")
    }
    else -> {
        suffix("ViewProxy")
    }
}

fun TypeName.toProxy():TypeName {
    return if (this is ParameterizedTypeName) {
        val newRaw:ClassName = rawType.let { ClassName(it.packageName, it.simpleName.toProxy()) }
        newRaw.parameterizedBy(typeArguments)
//        ParameterizedTypeName(rawType = newRaw, type)
    }
    else {
        (this as ClassName).let { ClassName(it.packageName, it.simpleName.toProxy()) }
    }
}




fun String.toView() = when {
    endsWith("VC") -> {
        substring(0, indexOf("VC")).suffix("View")
    }
    else -> {
        suffix("View")
    }
}

fun TypeName.toView() =
    (this as ClassName).let { ClassName(it.packageName, it.simpleName.toView()) }

