package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import tech.skot.core.components.ComponentVC
import tech.skot.core.components.Opens
import tech.skot.core.components.ScreenVC
import tech.skot.core.components.UIState
import tech.skot.tools.generation.viewmodel.toVM
import java.lang.IllegalStateException
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

data class ComponentDef(
        val name: String,
        val vc: KClass<out ComponentVC>,
        val packageName: String,
        val subComponents: List<PropertyDef>,
        val fixProperties: List<PropertyDef>,
        val mutableProperties: List<PropertyDef>,
        val state:ClassName?
) {

    fun proxy() = ClassName(packageName, name.suffix("ViewProxy"))
    fun rai() = ClassName(packageName, name.suffix("RAI"))
    fun viewModelGen() = ClassName(packageName, name.suffix("Gen"))
    fun viewModel() = ClassName(packageName, name)

    fun layoutName() = name.map { if (it.isUpperCase()) "_${it.toLowerCase()}" else it }.joinToString(separator = "").substring(1)
    fun binding(viewModuleAndroidPackage: String) = ClassName("$viewModuleAndroidPackage.databinding", name.suffix("Binding"))
    fun viewImpl() = ClassName(packageName, name.suffix("ViewImpl"))

    fun toFillVCparams() = (subComponents.toFillParams(init = { "${name.toVM()}.view" }) + fixProperties.toFillParams() + mutableProperties.map { it.initial() }.toFillParams()).joinToString(separator = ",")

    val isScreen = vc.isSubclassOf(ScreenVC::class)
}

fun KClass<out ComponentVC>.meOrSubComponentHasState():Boolean = nestedClasses.any { it.hasAnnotation<UIState>() } || ownProperties().any { it.returnType.isComponent() && (it.returnType.classifier as KClass<out ComponentVC>).meOrSubComponentHasState()}


data class PropertyDef(val name: String, val type: TypeName, val meOrSubComponentHasState:Boolean? = null) {
    fun asParam(): ParameterSpec = ParameterSpec.builder(name, type).build()
    fun initial(): PropertyDef = PropertyDef(name.initial(), type)

    val isLambda = (type as? ParameterizedTypeName)?.rawType?.simpleName?.startsWith("Function") ?: false

    fun initializer() =
        when {
            type.isNullable -> "null"
            (type as? ClassName)?.simpleName == "String" -> "\"???\""
            isLambda -> "{ ${name}() }"
            else -> (type as ClassName).simpleName + "??"

    }

}

fun List<PropertyDef>.toFillParams(init:(PropertyDef.()->String)? = null) = map { "${it.name} = ${init?.invoke(it) ?: it.initializer()}" }



fun MutableSet<KClass<out ComponentVC>>.addLinkedComponents(aComponentClass: KClass<out ComponentVC>, appPackageName:String) {
    add(aComponentClass)
    val subComponents = aComponentClass.ownProperties().map { it.returnType }.filter { it.isComponent() && (it.asTypeName() as ClassName).packageName.startsWith(appPackageName)}.map { it.jvmErasure as KClass<out ComponentVC> }
    subComponents.forEach {
        addLinkedComponents(it, appPackageName)
    }
    aComponentClass.findAnnotation<Opens>()?.let {
        (it as Opens).screenOpened.forEach {
            if (!contains(it)) {
                addLinkedComponents(it, appPackageName)
            }
        }
    }
}

fun KClass<out ComponentVC>.ownProperties(): List<KCallable<*>> {
    val superTypePropertiesNames = superclasses[0].members.filter { it is KProperty }.map { it.name }
    return members.filter { it is KProperty }.filter { !superTypePropertiesNames.contains(it.name) }
}

fun KClass<out ComponentVC>.def(): ComponentDef {

    val ownProperties = ownProperties()
    val subComponentsProperties = ownProperties.filter { it.returnType.isComponent() }
    val stateProperties = ownProperties - subComponentsProperties


    if (!simpleName!!.endsWith("VC")) {
        throw IllegalStateException("VC interface $qualifiedName must end with \"VC\"")
    }


    return ComponentDef(
            name = simpleName!!.withOut("VC"),
            vc = this,
            packageName = packageName(),
            subComponents = subComponentsProperties.map {
                if (it is KMutableProperty) {
                    throw IllegalStateException("SubComponent ${it.name} of ${this.packageName()}.${this.simpleName} is Mutable, it is not allowed !!!")
                }
                PropertyDef(it.name, it.returnType.asTypeName(), meOrSubComponentHasState = (it.returnType.classifier as KClass<out ComponentVC>).meOrSubComponentHasState())
            },
            fixProperties = stateProperties.filter { !(it is KMutableProperty) }.map {
                PropertyDef(it.name, it.returnType.asTypeName())
            },
            mutableProperties = stateProperties.filter { it is KMutableProperty }.map {
                PropertyDef(it.name, it.returnType.asTypeName())
            },
            state = nestedClasses.find { it.hasAnnotation<UIState>() }?.asClassName()
    )
}

@ExperimentalStdlibApi
fun KClass<*>.isScreenVC() = supertypes.contains(typeOf<ScreenVC>())

@ExperimentalStdlibApi
fun KClass<*>.isDescendantOf(kType: KType) = supertypes.contains(kType)

fun String.withOut(suffix: String) = substring(0, indexOf(suffix))
fun String.suffix(suffix: String) = "$this$suffix"

fun String.initial() = suffix("Initial")

fun KClass<*>.packageName() = this.java.`package`.name

val componentViewType = ComponentVC::class.createType()
val collectionType = Collection::class.createType(arguments = listOf(KTypeProjection(KVariance.OUT, componentViewType)))
fun KType.isComponent() = componentViewType.isSupertypeOf(this)
