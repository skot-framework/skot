package tech.skot.model

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class SKStates(val neededStates: Array<KClass<out SKState>>)

@Target(AnnotationTarget.CLASS)
annotation class SKSubStates(val subStates: Array<KClass<out SKState>>)
