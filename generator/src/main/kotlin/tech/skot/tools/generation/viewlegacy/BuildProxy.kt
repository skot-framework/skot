package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.core.components.SKLayoutIsRoot
import tech.skot.core.components.SKLayoutIsSimpleView
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKLegacyViewIncluded
import tech.skot.tools.generation.*
import tech.skot.tools.generation.AndroidClassNames.layoutInflater
import tech.skot.tools.generation.AndroidClassNames.viewGroup
import kotlin.io.path.exists
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation

const val coreComponentsPackage = "tech.skot.core.components"

val screenProxy = ClassName(coreComponentsPackage, "SKScreenViewProxy")
val componentProxy = ClassName(coreComponentsPackage, "SKComponentViewProxy")
val screenViewImpl = ClassName(coreComponentsPackage, "SKScreenView")
val componentViewImpl = ClassName(coreComponentsPackage, "SKComponentView")
val screenViewModel = ClassName(coreComponentsPackage, "SKScreen")
val componentViewModel = ClassName(coreComponentsPackage, "SKComponent")
val mutableSKLiveData = ClassName("tech.skot.view.live", "MutableSKLiveData")
val skMessage = ClassName("tech.skot.view.live", "SKMessage")


fun PropertyDef.ld() =
    PropertyDef(name = name.suffix("LD"), type = mutableSKLiveData.parameterizedBy(type))

fun PropertyDef.onMethod(vararg modifiers: KModifier, body: String? = null) =
    FunSpec.builder("on${name.capitalize()}")
        .addParameter(name, type).apply {
            addModifiers(modifiers = modifiers)
            body?.let { addCode(it) }
        }.build()

fun KFunction<*>.dataClassName() = "${name.capitalize()}Data"

fun ComponentDef.buildProxy(
    generator: Generator,
    viewModuleAndroidPackage: String,
    baseActivity: ClassName
): TypeSpec {


    return TypeSpec.classBuilder(proxy())
        .addPrimaryConstructorWithParams(
            subComponents.map {
                ParamInfos(
                    name = it.name,
                    typeName = it.type.toProxy(),
                    modifiers = listOf(KModifier.OVERRIDE),
                    isVal = true
                )
            } +
                    fixProperties.map {
                        ParamInfos(
                            name = it.name,
                            typeName = it.type,
                            modifiers = listOf(KModifier.OVERRIDE),
                            isVal = true
                        )
                    } +
                    mutableProperties.map {
                        ParamInfos(
                            name = it.name.initial(),
                            typeName = it.type,
                            isVal = false
                        )
                    }
        )
        .superclass(
            (if (isScreen) screenProxy else componentProxy).parameterizedBy(
                binding(
                    viewModuleAndroidPackage
                )
            )
        )
        .addSuperinterface(vc)
        .apply {
            val layoutPath = generator.androidResLayoutPath(generator.modules.view, layoutName())
            val includesIds: Set<String>? by lazy {
                if (layoutPath.exists()) {
                    try {
                        layoutPath.getDocument()
                            .getElementsWithTagName("include")
                            .map {
                                it.getAttribute("android:id")
                            }
                            .toSet()
                    } catch (ex: Exception) {
                        null
                    }
                } else {
                    null
                }
            }



            mutableProperties.forEach {
                val ld = it.ld()
                addProperty(
                    PropertySpec.builder(ld.name, ld.type)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer("MutableSKLiveData(${it.name.initial()})")
                        .build()
                )
                addProperty(
                    PropertySpec.builder(it.name, it.type)
                        .addModifiers(KModifier.OVERRIDE)
                        .mutable(true)
                        .delegate(ld.name)
                        .build()
                )
            }

            if (state != null) {
                addProperty(
                    PropertySpec.builder(
                        "saveSignal",
                        skMessage.parameterizedBy(Unit::class.asTypeName()),
                        KModifier.PRIVATE
                    )
                        .initializer("SKMessage()").build()
                )
                addProperty(
                    PropertySpec.builder("_state", state.nullable(), KModifier.PRIVATE)
                        .mutable(true)
                        .initializer("null").build()
                )
            }



            ownFunctions.forEach {
                val withParams = it.parameters.size > 1
                val dataClassName = it.dataClassName()
                if (withParams) {
                    addType(
                        TypeSpec.classBuilder(dataClassName)
                            .addModifiers(KModifier.DATA)
                            .addPrimaryConstructorWithParams(it.parameters.mapNotNull { kParam ->
                                kParam.name?.let {
                                    ParamInfos(it, kParam.type.asTypeName())
                                }
                            })
                            .build()
                    )
                }
                addProperty(
                    PropertySpec.builder(
                        "${it.name}Message", skMessage.parameterizedBy(
                            if (withParams) {
                                ClassName("", dataClassName)
                            } else {
                                Unit::class.asTypeName()
                            }
                        ), KModifier.PRIVATE
                    )
                        .initializer("SKMessage()").build()
                )
                fun postMessageParams(func: KFunction<*>) = if (func.parameters.size == 1) {
                    "Unit"
                } else {
                    it.parameters.filter { it.name != null }
                        .map { it.name!! }
                        .joinToString(prefix = "$dataClassName(", postfix = ")", separator = ", ")
                }
                addFunction(
                    FunSpec.builder(it.name)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameters(it.parameters.mapNotNull { kParam ->
                            kParam.name?.let {
                                ParameterSpec.builder(it, kParam.type.asTypeName()).build()
                            }
                        })
                        .addCode("${it.name}Message.post(${postMessageParams(it)})")
                        .build()
                )

            }


//            if (state != null || subComponents.any { it.meOrSubComponentHasState == true }) {
            addFunction(FunSpec.builder("saveState")
                .addModifiers(KModifier.OVERRIDE)
                .apply {
                    if (state != null) {
                        addStatement("saveSignal.post(Unit)")
                    }
                    subComponents.forEach {
//                                if (it.meOrSubComponentHasState == true) {
                        addStatement("${it.name}.saveState()")
//                                }
                    }
                }
                .build())
//            }


            if (isScreen) {
                addFunction(
                    FunSpec.builder("getActivityClass")
                        .addModifiers(KModifier.OVERRIDE)
                        .addCode(
                            activityClass?.let { "return $it::class.java" } ?: generator.baseActivityVar?.let { "return $it" } ?: "return ${baseActivity.packageName}.${baseActivity.simpleName}::class.java")
                        .build()
                )
            }

            if (hasLayout) {
                addProperty(
                    PropertySpec.builder("layoutId", ClassName("kotlin", "Int"))
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("R.layout.${layoutName()}")
                        .build()
                )

                addFunction(
                    FunSpec.builder("bindingOf")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("view", AndroidClassNames.view)
                        .returns(binding(viewModuleAndroidPackage))
                        .addStatement("return ${binding(viewModuleAndroidPackage).simpleName}.bind(view)")
                        .build()
                )

            }

            addFunction(
                FunSpec.builder("inflate")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("layoutInflater", layoutInflater)
                    .addParameter("parent", viewGroup.nullable())
                    .addParameter("attachToParent", Boolean::class)
                    .returns(binding(viewModuleAndroidPackage))
                    .addCode("return ${binding(viewModuleAndroidPackage).simpleName}.inflate(layoutInflater, parent, attachToParent)")
                    .build()
            )

            addFunction(
                FunSpec.builder("bindTo")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("activity", AndroidClassNames.skActivity)
                    .addParameter("fragment", AndroidClassNames.fragment.nullable())
                    .addParameter("binding", binding(viewModuleAndroidPackage))
                    .addParameter("collectingObservers", ClassName("kotlin", "Boolean"))
                    .returns(viewImpl())

                    .beginControlFlow("return ${viewImpl().simpleName}(this, activity, fragment, binding${
                        subComponents.filter { it.passToParentView }
                            .map {
                                ", ${
                                    it.bindToSubComponent(
                                        generator,
                                        includesIds
                                    )
                                } as ${it.viewImplClassName.simpleName}"
                            }.joinToString()
                    }).apply"
                    )
                    .addStatement("collectObservers = collectingObservers")
                    .apply {

                        subComponents.filter { !it.passToParentView }.forEach {
                            addStatement(
                                it.bindToSubComponent(
                                    generator,
                                    includesIds
                                )
                            )
                        }

                        if (fixProperties.isNotEmpty() || mutableProperties.isNotEmpty() || state != null || ownFunctions.isNotEmpty()) {
                            fixProperties.forEach {
                                addStatement("${it.onMethod().name}(${it.name})")
                            }
                            mutableProperties.forEach {
                                beginControlFlow("${it.ld().name}.observe")
                                addStatement("${it.onMethod().name}(it)")
                                endControlFlow()
                            }

                            if (state != null) {
                                beginControlFlow("saveSignal.observe")
                                addStatement("_state = saveState()")
                                endControlFlow()
                                addStatement("_state?.let { restoreState(it) }")
                            }
                            ownFunctions.forEach {
                                beginControlFlow("${it.name}Message.observe")
                                addStatement("this.${it.name}(${
                                    it.parameters.filter { it.name != null }
                                        .map { "it.${it.name}" }
                                        .joinToString()
                                })")
                                endControlFlow()
                            }
                        }

                    }
                    .endControlFlow()
                    .build()
            )
        }
        .build()
}

fun PropertyDef.bindToSubComponent(generator: Generator, includesIds: Set<String>?): String {

    fun tagIsInclude() = includesIds?.contains("@+id/$name") == true

    val klass = type.kClass()
    val bindToView =
        inPackage(generator.appPackage) == false && !klass.hasAnnotation<SKLayoutNo>() && !klass.hasAnnotation<SKLayoutIsRoot>() && !klass.hasAnnotation<SKLayoutIsSimpleView>()

    fun KClass<*>.binding(name: String): String =
        when {
            hasAnnotation<SKLayoutNo>() -> "Unit"
            hasAnnotation<SKLegacyViewIncluded>() || (hasAnnotation<SKLayoutIsSimpleView>() && tagIsInclude()) -> "binding.$name.root"
            hasAnnotation<SKLayoutIsRoot>() -> "binding.root"
            else -> "binding.$name"
        }

    return "${name}.${if (bindToView) "bindToView" else "_bindTo"}(activity, fragment, ${
        klass.binding(
            name
        )
    })"
}

fun ComponentDef.buildRAI(viewModuleAndroidPackage: String): TypeSpec =
    TypeSpec.interfaceBuilder(rai())
        .apply {
            if (state != null) {
                addFunction(
                    FunSpec.builder("saveState").addModifiers(KModifier.ABSTRACT).returns(state)
                        .build()
                )
                addFunction(
                    FunSpec.builder("restoreState").addParameter("state", state)
                        .addModifiers(KModifier.ABSTRACT).build()
                )
            }
            fixProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
            mutableProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
            ownFunctionsNotInInterface.forEach {
                addFunction(
                    FunSpec.builder(it.name)
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameters(it.parameters.mapNotNull { kParam ->
                            kParam.name?.let {
                                ParameterSpec.builder(it, kParam.type.asTypeName()).build()
                            }
                        })
                        .build()
                )

            }
        }
        .build()

fun TypeName.kClass() =
    Class.forName(
        when (this) {
            is ParameterizedTypeName -> rawType
            else -> this as ClassName
        }.canonicalName
    ).kotlin
//    Class.forName((this as ClassName).canonicalName).kotlin

fun KClass<*>.binding(name: String): String =
    when {
        hasAnnotation<SKLayoutNo>() -> "Unit"
        hasAnnotation<SKLegacyViewIncluded>() -> "binding.$name.root"
        hasAnnotation<SKLayoutIsRoot>() -> "binding.root"
        else -> "binding.$name"
    }

