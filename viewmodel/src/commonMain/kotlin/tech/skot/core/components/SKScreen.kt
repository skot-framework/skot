package tech.skot.core.components

import tech.skot.core.components.presented.SKBottomSheet
import tech.skot.core.view.SKTransition


abstract class SKScreen<V : SKScreenVC>: SKComponent<SKScreenVC>() {
    var parent: SKStack? = null
    var presenter: SKBottomSheet? = null

    fun push(screen: SKScreen<*>) {
        parent?.push(screen) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun finish(transition:SKTransition? = null) {
        parent?.remove(this, transition) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
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