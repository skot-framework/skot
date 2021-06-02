package tech.skot.model

import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
annotation class SKStates(val neededStates: Array<KClass<out SKStateDef>>)

@Target(AnnotationTarget.CLASS)
annotation class SKBms(val bMs: Array<String>)
