package tech.skot.core.components

import kotlin.reflect.KClass

interface ComponentView {
    fun onRemove()
}

interface UiState<D> {
    var value:D?
}

@Target(AnnotationTarget.CLASS)
annotation class Model(val modelInterface: KClass<*>)

interface ScreenView: ComponentView {
    var onBackPressed:(()->Unit)?
}