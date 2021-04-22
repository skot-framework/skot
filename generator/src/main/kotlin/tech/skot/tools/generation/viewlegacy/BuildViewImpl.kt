package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*

const val TODO_GENERATED_BUT_NOT_IMPLEMENTED = "TODO(\"generated but still not implemented\")"

fun ComponentDef.buildViewImpl(viewModuleAndroidPackage:String) =
        TypeSpec.classBuilder(viewImpl().simpleName)
                .addPrimaryConstructorWithParams(
                        listOfNotNull(
                                ParamInfos("activity", AndroidClassNames.skActivity, isVal = false),
                                ParamInfos("fragment", AndroidClassNames.fragment.nullable(), isVal = false),
                                ParamInfos("binding", binding(viewModuleAndroidPackage), isVal = false)
                        )+
                                subComponents.filter { it.passToParentView }.map {
                                    ParamInfos(it.name, it.type.toView(), isVal = false)
                                }
                )
                .superclass((if (isScreen) screenViewImpl else componentViewImpl).parameterizedBy(binding(viewModuleAndroidPackage)))
                .addSuperinterface(rai())
                .addSuperclassConstructorParameter("activity")
                .addSuperclassConstructorParameter("fragment")
                .addSuperclassConstructorParameter("binding")
                .addFunctions(
                        fixProperties.map { it.onMethod(KModifier.OVERRIDE, body = it.generatedBodyForProperty()) } +
                                mutableProperties.map { it.onMethod(KModifier.OVERRIDE, body = it.generatedBodyForProperty()) }
                )
                .apply {
                    if (state != null) {
                        addFunction(FunSpec.builder("saveState").addModifiers(KModifier.OVERRIDE).returns(state).addCode(TODO_GENERATED_BUT_NOT_IMPLEMENTED).build())
                        addFunction(FunSpec.builder("restoreState").addParameter("state", state).addModifiers(KModifier.OVERRIDE).addCode(TODO_GENERATED_BUT_NOT_IMPLEMENTED).build())
                    }
                    ownFunctions.forEach {
                        addFunction(
                                FunSpec.builder(it.name)
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addParameters(it.parameters.mapNotNull { kParam ->
                                            kParam.name?.let {
                                                ParameterSpec.builder(it, kParam.type.asTypeName()).build()
                                            }
                                        })
                                        .addCode(TODO_GENERATED_BUT_NOT_IMPLEMENTED)
                                        .build()
                        )

                    }
                }
                .build()


fun PropertyDef.generatedBodyForProperty():String? {
    return when(type.simpleName())  {
       "String" -> {
            "binding.tv${name.capitalize()}.text = $name"
        }
        "Function0" -> {
            "binding.btn${name.substringAfter("onTap")}.setOnClick($name)"
        }
        else -> TODO_GENERATED_BUT_NOT_IMPLEMENTED
    }

}