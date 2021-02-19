package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeName
import tech.skot.tools.generation.*
import java.nio.file.Files

fun Generator.generateViewLegacy() {
    deleteModuleGenerated(Modules.view)
    val viewModuleAndroidPackage = getAndroidPackageName(rootPath.resolve(Modules.view).resolve("src/androidMain"))
    components.forEach {
        FileSpec.builder(
                it.proxy().packageName,
                it.proxy().simpleName
        )
                .addType(it.buildProxy(viewModuleAndroidPackage, baseActivity))
                .addType(it.buildRAI(viewModuleAndroidPackage))
                .apply {
                    if (it.hasLayout) {
                        addImportClassName(viewR)
                    }
                }.build()
                .writeTo(generatedAndroidSources(Modules.view))
        if (!it.viewImpl().existsAndroidInModule(Modules.view)) {
            FileSpec.builder(
                    it.viewImpl().packageName,
                    it.viewImpl().simpleName)
                    .addType(it.buildViewImpl(viewModuleAndroidPackage))
                    .build()
                    .writeTo(androidSources(Modules.view))
        }
        val layoutPath = androidResLayoutPath(Modules.view, it.layoutName())
        if (!Files.exists(layoutPath)) {
            layoutPath.toFile().writeText(LAYOUT_TEMPLATE)
        }

    }


    generateViewLegacyInjectorImpl(Modules.view)
}

const val LAYOUT_TEMPLATE = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
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

fun TypeName.toProxy() = (this as ClassName).let { ClassName(it.packageName, it.simpleName.toProxy()) }


