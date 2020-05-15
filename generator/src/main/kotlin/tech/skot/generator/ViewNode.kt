package tech.skot.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.components.ComponentView
import tech.skot.contract.ComponentHasParameter
import tech.skot.contract.Private
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

class ViewNode(val viewClass: KClass<out ComponentView>, val children: List<ViewNode>? = null)

fun KClass<out ComponentView>.node(vararg children: ViewNode) = ViewNode(this, children = children.asList())






fun KClass<*>.ownMembers(): List<KCallable<*>> {
    val superMembersNames = superclasses.flatMap { it.members.map { it.name } }
    return members.filter { !superMembersNames.contains(it.name) }
}

fun KClass<*>.ownFuncs() = ownMembers().filterIsInstance(KFunction::class.java)

val componentViewType = ComponentView::class.createType()
val collectionType = Collection::class.createType(arguments = listOf(KTypeProjection(KVariance.OUT, componentViewType)))

fun KType.isComponentView() = isSubtypeOf(componentViewType)
fun KType.isCollectionOfComponentView() = isSubtypeOf(collectionType)

fun KClass<out ComponentView>.subComponents() =
        ownMembers()
                .map { it.returnType }
                .mapNotNull { it.componentView() }

fun KClass<out ComponentView>.superComponents():List<KClass<out ComponentView>> =
        supertypes
                .mapNotNull { it.componentView() }.flatMap { listOf(it) + it.superComponents() }

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

fun <V : Any> Collection<KClass<out V>>.fromApp() = filter { it?.isFromApp() == true }
fun KClass<*>.isFromApp() = packageName().startsWith(appPackageName)

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



fun KClass<out ComponentView>.allSubComponentMembers() = subComponentMembers() + superComponents().flatMap { it.subComponentMembers() }


fun KClass<out ComponentView>.propertyMember() =
        ownMembers()
                .filterIsInstance(KProperty::class.java)
                .filter {
                    !it.returnType.isComponentView() && !it.returnType.isCollectionOfComponentView()
                }

fun KClass<out ComponentView>.allPropertyMember() = propertyMember() + superComponents().flatMap { it.propertyMember().filter { it.annotations.none { it is Private  } } }


fun KClass<out ComponentView>.compName() = simpleName!!.substringBefore("View")

fun KType.componentClassName(parametrizedIfNeeded:Boolean = false): TypeName? {
    if (isComponentView()) {
        val viewKlass = (jvmErasure as? KClass<out ComponentView>)!!
        val className =  viewKlass.componentClassName()
        return if (parametrizedIfNeeded && viewKlass.findAnnotation<ComponentHasParameter>() != null) {
            className.parameterizedBy(WildcardTypeName.producerOf(ANY))
        }
        else {
            className
        }
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

fun KType.viewImplClassName(): TypeName? {
    if (isComponentView()) {
        return (jvmErasure as? KClass<out ComponentView>)!!.viewImplClassName()
    } else if (isCollectionOfComponentView()) {
        val parametrizedType = this.asTypeName() as ParameterizedTypeName
        return ClassName(parametrizedType.rawType.packageName, parametrizedType.rawType.simpleName)
                .parameterizedBy(
                        arguments.map {
                            it.type?.viewImplClassName() ?: it.type!!.asTypeName()
                        }
                )
    } else {
        return null
    }
}

fun KType.viewImplClassNameName(): String? {
    if (isComponentView()) {
        return (jvmErasure as? KClass<out ComponentView>)!!.viewImplClassName().simpleName
    } else if (isCollectionOfComponentView()) {
        val parametrizedType = this.asTypeName() as ParameterizedTypeName
        return "${parametrizedType.rawType.simpleName}<${arguments.map {
                                        it.type?.viewImplClassNameName() ?: it.type!!.asTypeName()
                        }}>"
//        ClassName(parametrizedType.rawType.packageName, parametrizedType.rawType.simpleName)
//                .parameterizedBy(
//                        arguments.map {
//                            it.type?.viewImplClassName() ?: it.type!!.asTypeName()
//                        }
//                ).
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

