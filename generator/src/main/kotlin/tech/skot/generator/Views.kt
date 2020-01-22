package tech.skot.generator

import tech.skot.contract.viewcontract.ComponentView
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure

class ViewNode(val viewClass: KClass<out ComponentView>, val children: List<ViewNode>? = null)

fun KClass<out ComponentView>.node(vararg children: ViewNode) = ViewNode(this, children = children.asList())


fun KClass<*>.ownMembers(): List<KCallable<*>> {
    val superMembersNames = superclasses[0].members.map { it.name }
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

fun KType.componentView(): KClass<out ComponentView>? {
    if (isComponentView()) {
        return jvmErasure as? KClass<out ComponentView>
    } else if (isCollectionOfComponentView()) {
        return arguments[0].type?.componentView()
    } else {
        return null
    }
}

fun <V:Any>List<KClass<out V>>.fromApp() = filter { it?.packageName()?.startsWith(appPackageName) == true}
