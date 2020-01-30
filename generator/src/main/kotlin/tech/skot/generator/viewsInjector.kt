package tech.skot.generator

import tech.skot.components.ComponentView
import tech.skot.components.ScreenView
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

var _views: ViewNode? = null

fun initGenerator(views: ViewNode) {
    _views = views
}

val actualComponents: Set<KClass<out ComponentView>> by lazy {
    _views!!.allActualComponents()
}

val allComponents: Set<KClass<out ComponentView>> by lazy {
    _views!!.allComponents()
}

val componentsFromApp: List<KClass<out ComponentView>> by lazy {
    allComponents.fromApp()
}

val actualComponentsFromApp: List<KClass<out ComponentView>> by lazy {
    componentsFromApp.filter { it.isActualComponent() }
}


fun KClass<out ComponentView>.isActualComponent() = actualComponents.contains(this)

fun KClass<out ComponentView>.isScreenView() = this.isSubclassOf(ScreenView::class)


val actions by lazy {
    allComponents.flatMap { it.actions() }.toSet().map { it.jvmErasure }
}

val actionsFromApp: List<KClass<*>> by lazy {
    actions.fromApp()
}

