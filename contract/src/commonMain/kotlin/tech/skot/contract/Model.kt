package tech.skot.contract

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Model(val modelInterface: KClass<*>)
