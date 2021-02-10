package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.tools.generation.ComponentDef
import tech.skot.tools.generation.ParamInfos
import tech.skot.tools.generation.addPrimaryConstructorWithParams
import tech.skot.tools.generation.nullable

fun ComponentDef.buildViewImpl(viewModuleAndroidPackage:String) =
        TypeSpec.classBuilder(viewImpl().simpleName)
                .addPrimaryConstructorWithParams(
                        listOf(
                                ParamInfos("activity", skActivity, isVal = false),
                                ParamInfos("fragment", fragment.nullable(), isVal = false),
                                ParamInfos("binding", binding(viewModuleAndroidPackage), isVal = false)
                        )
                )
                .superclass((if (isScreen) screenViewImpl else componentViewImpl).parameterizedBy(binding(viewModuleAndroidPackage)))
                .addSuperinterface(rai())
                .addSuperclassConstructorParameter("activity")
                .addSuperclassConstructorParameter("fragment")
                .addSuperclassConstructorParameter("binding")
                .addFunctions(
                        fixProperties.map { it.onMethod(KModifier.OVERRIDE, body = "TODO(\"generated but still not implemented\")") } +
                                mutableProperties.map { it.onMethod(KModifier.OVERRIDE, body = "TODO(\"generated but still not implemented\")") }
                )
                .build()