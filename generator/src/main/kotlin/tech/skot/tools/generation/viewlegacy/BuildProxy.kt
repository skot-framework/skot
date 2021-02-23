package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.core.components.IdLayout
import tech.skot.core.components.LayoutIsRoot
import tech.skot.core.components.NoLayout
import tech.skot.tools.generation.*
import kotlin.reflect.full.hasAnnotation

const val coreComponentsPackage = "tech.skot.core.components"
val layoutInflater = ClassName("android.view", "LayoutInflater")
val viewGroup = ClassName("android.view", "ViewGroup")

val screenProxy = ClassName(coreComponentsPackage, "ScreenViewProxy")
val componentProxy = ClassName(coreComponentsPackage, "ComponentViewProxy")
val screenViewImpl = ClassName(coreComponentsPackage, "ScreenViewImpl")
val componentViewImpl = ClassName(coreComponentsPackage, "ComponentViewImpl")
val screenViewModel = ClassName(coreComponentsPackage, "Screen")
val componentViewModel = ClassName(coreComponentsPackage, "Component")
val mutableSKLiveData = ClassName("tech.skot.view.live", "MutableSKLiveData")
val skMessage = ClassName("tech.skot.view.live", "SKMessage")




fun PropertyDef.ld() = PropertyDef(name = name.suffix("LD"), type = mutableSKLiveData.parameterizedBy(type))
fun PropertyDef.onMethod(vararg modifiers: KModifier, body: String? = null) = FunSpec.builder("on${name.capitalize()}")
        .addParameter(name, type).apply {
            addModifiers(modifiers = modifiers)
            body?.let { addCode(it) }
        }.build()


fun ComponentDef.buildProxy(viewModuleAndroidPackage: String, baseActivity: ClassName?): TypeSpec = TypeSpec.classBuilder(proxy())
        .addPrimaryConstructorWithParams(
                subComponents.map { ParamInfos(name = it.name, typeName = it.type.toProxy(), modifiers = listOf(KModifier.OVERRIDE), isVal = true) } +
                        fixProperties.map { ParamInfos(name = it.name, typeName = it.type, modifiers = listOf(KModifier.OVERRIDE), isVal = true) } +
                        mutableProperties.map { ParamInfos(name = it.name.initial(), typeName = it.type, isVal = false) }
        )
        .superclass((if (isScreen) screenProxy else componentProxy).parameterizedBy(binding(viewModuleAndroidPackage)))
        .addSuperinterface(vc)
        .apply {
            mutableProperties.forEach {
                val ld = it.ld()
                addProperty(PropertySpec.builder(ld.name, ld.type)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer("MutableSKLiveData(${it.name.initial()})")
                        .build())
                addProperty(PropertySpec.builder(it.name, it.type)
                        .addModifiers(KModifier.OVERRIDE)
                        .mutable(true)
                        .delegate(ld.name)
                        .build())
            }

            if (state != null) {
                addProperty(PropertySpec.builder("saveSignal", skMessage.parameterizedBy(Unit::class.asTypeName()), KModifier.PRIVATE)
                        .initializer("SKMessage()").build())
                addProperty(PropertySpec.builder("_state", state.nullable(), KModifier.PRIVATE).mutable(true).initializer("null").build())
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


            if (isScreen && baseActivity != null) {
                addFunction(
                        FunSpec.builder("getActivityClass")
                                .addModifiers(KModifier.OVERRIDE)
                                .addCode("return ${baseActivity.packageName}${baseActivity.simpleName}::class.java")
                                .build())
            }

            if (hasLayout) {
                addProperty(
                        PropertySpec.builder("layoutId", ClassName("kotlin", "Int"))
                                .addModifiers(KModifier.OVERRIDE)
                                .initializer("R.layout.${layoutName()}")
                                .build())

                addFunction(FunSpec.builder("bind")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("view", AndroidClassNames.view)
                        .returns(binding(viewModuleAndroidPackage))
                        .addStatement("return ${binding(viewModuleAndroidPackage).simpleName}.bind(view)")
                        .build())

            }

//            if (isScreen) {
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
//            }
        }
        .addFunction(
                FunSpec.builder("bindTo")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("activity", AndroidClassNames.skActivity)
                        .addParameter("fragment", AndroidClassNames.fragment.nullable())
                        .addParameter("binding", binding(viewModuleAndroidPackage))
                        .addParameter("collectingObservers", ClassName("kotlin", "Boolean"))
                        .returns(viewImpl())

                        .beginControlFlow("return ${viewImpl().simpleName}(activity, fragment, binding).apply")
                        .addStatement("collectObservers = collectingObservers")
                        .apply {
                            subComponents.forEach {
                                addStatement("${it.name}.bindTo(activity, fragment, ${it.type.binding(it.name)})")
                            }

                            if (fixProperties.isNotEmpty() || mutableProperties.isNotEmpty() || state != null) {
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
                            }
                            else {
                                addStatement("return ${viewImpl().simpleName}(activity, fragment, binding)")
                            }

                        }
                        .endControlFlow()
                        .build()
        )
        .build()

fun ComponentDef.buildRAI(viewModuleAndroidPackage: String): TypeSpec = TypeSpec.interfaceBuilder(rai())
        .apply {
            if (state != null) {
                addFunction(FunSpec.builder("saveState").addModifiers(KModifier.ABSTRACT).returns(state).build())
                addFunction(FunSpec.builder("restoreState").addParameter("state", state).addModifiers(KModifier.ABSTRACT).build())
            }
            fixProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
            mutableProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
        }
        .build()

fun TypeName.binding(name: String): String = (this as ClassName).let {
    val kClass = Class.forName(it.canonicalName).kotlin
    return when {
        kClass.hasAnnotation<NoLayout>() -> "Unit"
        kClass.hasAnnotation<LayoutIsRoot>() -> "binding.root"
        kClass.hasAnnotation<IdLayout>() -> "binding.$name.id"
        else -> "binding.$name"
    }
}