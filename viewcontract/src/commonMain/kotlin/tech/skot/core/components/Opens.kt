package tech.skot.core.components

import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
annotation class Opens(val screensOpened: Array<KClass<out ScreenVC>>)
