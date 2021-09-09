package tech.skot.core.components

import tech.skot.core.components.presented.SKBottomSheet
import tech.skot.core.view.SKTransition


abstract class SKScreen<V : SKScreenVC>: SKComponent<SKScreenVC>(), SKVisiblityListener {
    var parent: SKStack? = null
    var presenter: SKBottomSheet? = null

    //CallSuper!!
    override fun onResume(){
    }
    //CallSuper!!
    override fun onPause(){
    }


    fun push(screen: SKScreen<*>) {
        parent?.push(screen) ?:
            SKRootStack.push(screen)
//        throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun removeAllScreensOnTop() {
        parent?.let { stack ->
            val currentStackScreens = stack.state.screens
            val indexOfThisScreen = currentStackScreens.indexOf(this)
            if (indexOfThisScreen != -1 && currentStackScreens.size > indexOfThisScreen + 1) {
                stack.state = SKStack.State(screens = currentStackScreens.subList(0, indexOfThisScreen +1))
            }
        } ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
    }

    fun replaceWith(screen: SKScreen<*>) {
        parent?.replace(this, screen) ?: throw IllegalStateException("This ${this::class.simpleName} has currently no stack parent")
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