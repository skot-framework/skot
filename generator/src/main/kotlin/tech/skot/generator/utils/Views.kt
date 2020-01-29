package tech.skot.generator.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import tech.skot.components.ComponentView
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure

class ViewNode(val viewClass: KClass<out ComponentView>, val children: List<ViewNode>? = null)

fun KClass<out ComponentView>.node(vararg children: ViewNode) = ViewNode(this, children = children.asList())






fun KClass<*>.ownMembers(): List<KCallable<*>> {
    val superMembersNames = superclasses.flatMap { it.members.map { it.name } }
    return members.filter { !superMembersNames.contains(it.name) }
}

val componentViewType = ComponentView::class.createType()
val collectionType = Collection::class.createType(arguments = listOf(KTypeProjection(KVariance.OUT, componentViewType)))

fun KType.isComponentView() = isSubtypeOf(componentViewType)
fun KType.isCollectionOfComponentView() = isSubtypeOf(collectionType)

fun KClass<out ComponentView>.subComponents() =
        ownMembers()
                .map { it.returnType }
                .mapNotNull { it.componentView() }

fun KClass<out ComponentView>.superComponents() =
        supertypes
                .mapNotNull { it.componentView() }

fun KClass<out ComponentView>.superComponentView() = superclasses[0] as KClass<out ComponentView>
fun KClass<out ComponentView>.componentClassName() = ClassName(packageName(), compName())
fun KClass<out ComponentView>.superComponentClassName() = superComponentView().componentClassName()

fun KType.componentView(): KClass<out ComponentView>? {
    if (isComponentView()) {
        return jvmErasure as? KClass<out ComponentView>
    } else if (isCollectionOfComponentView()) {
        return arguments[0].type?.componentView()
    } else {
        return null
    }
}

fun <V : Any> Collection<KClass<out V>>.fromApp() = filter { it?.packageName()?.startsWith(appPackageName) == true }

fun ViewNode.allComponents(): Set<KClass<out ComponentView>> {
    return viewClass.allComponents() + (children?.flatMap { it.allComponents() } ?: emptySet())
}
fun KClass<out ComponentView>.allComponents(): Set<KClass<out ComponentView>> {
    return setOf(this) + subComponents().flatMap { it.allComponents() } + superComponents().flatMap { it.allComponents() }
}

//without abstract
fun ViewNode.allActualComponents(): Set<KClass<out ComponentView>> {
    return viewClass.allActualComponents() + (children?.flatMap { it.allActualComponents() } ?: emptySet())
}
fun KClass<out ComponentView>.allActualComponents(): Set<KClass<out ComponentView>> {
    return setOf(this) + subComponents().flatMap { it.allActualComponents() }
}


fun KClass<out ComponentView>.subComponentMembers() =
        ownMembers()
                .filter { it is KProperty }
                .filter {
                    it.returnType.isComponentView() || it.returnType.isCollectionOfComponentView()
                }

fun KClass<out ComponentView>.propertyMember() =
        ownMembers()
                .filterIsInstance(KProperty::class.java)
                .filter {
                    !it.returnType.isComponentView() && !it.returnType.isCollectionOfComponentView()
                }

fun KClass<out ComponentView>.compName() = simpleName!!.substringBefore("View")

fun KType.componentClassName(): TypeName? {
    if (isComponentView()) {
        return (jvmErasure as? KClass<out ComponentView>)!!.componentClassName()
    } else if (isCollectionOfComponentView()) {
        val parametrizedType = this.asTypeName() as ParameterizedTypeName
        return ClassName(parametrizedType.rawType.packageName, parametrizedType.rawType.simpleName)
                .parameterizedBy(
                        arguments.map {
                            it.type?.componentClassName() ?: it.type!!.asTypeName()
                        }
                )
    } else {
        return null
    }
}

fun KCallable<*>.componentToView() =
        if (returnType.isCollectionOfComponentView()) {
            "$name.map { it.view }"
        } else {
            "$name.view"
        }

fun KClass<out ComponentView>.actions() = supertypes.filter { !it.isComponentView() && !(it.isAny()) }
