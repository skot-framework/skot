package tech.skot.contract

import tech.skot.components.ComponentView
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class UseComponent(val usedComponent: KClass<out ComponentView>)
