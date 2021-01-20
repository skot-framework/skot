package tech.skot.core.components

import tech.skot.core.components.presented.BottomSheet


interface ScreenParent {
    fun push(screen: Screen<*>)
    fun remove(screen: Screen<*>)
}

abstract class Screen<V : ScreenView>: Component<V>() {
    var parent:ScreenParent? = null
    var presenter:BottomSheet? = null

    fun push(screen:Screen<*>) {
        parent?.push(screen) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun finish() {
        parent?.remove(this) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun dismiss() {
        presenter?.dismiss() ?: throw IllegalStateException("This ${this::class.simpleName} is not currently displayed as a BottomSheet")
    }
}