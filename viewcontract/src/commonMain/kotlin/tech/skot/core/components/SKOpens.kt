package tech.skot.core.components

import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
annotation class SKOpens(val screensOpened: Array<KClass<out SKScreenVC>>)
