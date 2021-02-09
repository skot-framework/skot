package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*
import tech.skot.core.components.NoLayout
import tech.skot.tools.generation.viewmodel.toVM
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.superclasses

const val coreComponentsPackage = "tech.skot.core.components"
val layoutInflater = ClassName("android.view", "LayoutInflater")

val screenProxy = ClassName(coreComponentsPackage, "ScreenViewProxy")
val screenViewImpl = ClassName(coreComponentsPackage, "ScreenViewImpl")
val screenViewModel = ClassName(coreComponentsPackage, "Screen")
val skActivity = ClassName(coreComponentsPackage, "SKActivity")
val skFragment = ClassName(coreComponentsPackage, "SKFragment")
val mutableSKLiveData = ClassName("tech.skot.view.live", "MutableSKLiveData")

fun PropertyDef.ld() = PropertyDef(name = name.suffix("LD"), type = mutableSKLiveData.parameterizedBy(type))
fun PropertyDef.onMethod(vararg modifiers: KModifier,body:String? = null) = FunSpec.builder("on${name.capitalize()}")
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
        .superclass(screenProxy.parameterizedBy(binding(viewModuleAndroidPackage)))
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

            if (baseActivity != null) {
                addFunction(
                        FunSpec.builder("getActivityClass")
                                .addModifiers(KModifier.OVERRIDE)
                                .addCode("return ${baseActivity.packageName}${baseActivity.simpleName}::class.java")
                                .build())
            }
        }
        .addFunction(
                FunSpec.builder("inflate")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("layoutInflater", layoutInflater)
                        .returns(binding(viewModuleAndroidPackage))
                        .addCode("return ${binding(viewModuleAndroidPackage).simpleName}.inflate(layoutInflater)")
                        .build()
        )
        .addFunction(
                FunSpec.builder("bindTo")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter("activity", skActivity)
                        .addParameter("fragment", skFragment.nullable())
                        .addParameter("layoutInflater", layoutInflater)
                        .addParameter("binding", binding(viewModuleAndroidPackage))
                        .returns(screenViewImpl.parameterizedBy(binding(viewModuleAndroidPackage)))
                        .apply {
                            subComponents.forEach {
                                addStatement("${it.name}.bindTo(activity, fragment, layoutInflater, ${it.type.binding()})")
                            }
                        }
                        .beginControlFlow("return ${viewImpl().simpleName}(activity, fragment, binding).apply")
                        .apply {
                            fixProperties.forEach {
                                addStatement("${it.onMethod().name}(${it.name})")
                            }
                            mutableProperties.forEach {
                                beginControlFlow("${it.ld().name}.observe")
                                addStatement("${it.onMethod().name}(it)")
                                endControlFlow()
                            }
                        }
                        .endControlFlow()
                        .build()
        )

        .build()

fun ComponentDef.buildRAI(viewModuleAndroidPackage: String): TypeSpec = TypeSpec.interfaceBuilder(rai())
        .apply {
            fixProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
            mutableProperties.forEach {
                addFunction(it.onMethod(KModifier.ABSTRACT))
            }
        }
        .build()

fun TypeName.binding():String =  (this as ClassName).let {
    if (Class.forName(it.canonicalName).kotlin.findAnnotation<NoLayout>() != null) "Unit" else "binding.${it.toVM().simpleName.decapitalize()}"
}