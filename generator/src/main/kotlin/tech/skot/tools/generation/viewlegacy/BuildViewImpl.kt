package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.tools.generation.ComponentDef
import tech.skot.tools.generation.ParamInfos
import tech.skot.tools.generation.addPrimaryConstructorWithParams
import tech.skot.tools.generation.nullable

const val TODO_GENERATED_BUT_NOT_IMPLEMENTED = "TODO(\"generated but still not implemented\")"

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
                        fixProperties.map { it.onMethod(KModifier.OVERRIDE, body = TODO_GENERATED_BUT_NOT_IMPLEMENTED) } +
                                mutableProperties.map { it.onMethod(KModifier.OVERRIDE, body = TODO_GENERATED_BUT_NOT_IMPLEMENTED) }
                )
                .apply {
                    if (state != null) {
                        addFunction(FunSpec.builder("saveState").addModifiers(KModifier.OVERRIDE).returns(state).addCode(TODO_GENERATED_BUT_NOT_IMPLEMENTED).build())
                        addFunction(FunSpec.builder("restoreState").addParameter("state", state).addModifiers(KModifier.OVERRIDE).addCode(TODO_GENERATED_BUT_NOT_IMPLEMENTED).build())
                    }
                }
                .build()