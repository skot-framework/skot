package tech.skot.core.components

import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
annotation class Uses(val usedComponents: Array<KClass<out ComponentVC>>)
