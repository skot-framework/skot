package tech.skot.core.components

import tech.skot.core.components.presented.SKBottomSheet


interface SKScreenParent {
    fun push(screen: SKScreen<*>)
    fun remove(screen: SKScreen<*>)
}

abstract class SKScreen<V : SKScreenVC>: SKComponent<SKScreenVC>() {
    var parent: SKScreenParent? = null
    var presenter: SKBottomSheet? = null

    fun push(screen: SKScreen<*>) {
        parent?.push(screen) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun finish() {
        parent?.remove(this) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun finishIfInAStack() {
        parent?.remove(this)
    }

    fun dismiss() {
        presenter?.dismiss() ?: throw IllegalStateException("This ${this::class.simpleName} is not currently displayed as a BottomSheet")
    }

    fun dismissIfBottomSheet() {
        presenter?.dismiss()
    }


}