package tech.skot.core.components

import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
annotation class SKUses(val usedComponents: Array<KClass<out ComponentVC>>)
