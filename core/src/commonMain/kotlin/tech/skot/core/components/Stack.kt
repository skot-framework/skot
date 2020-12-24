package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.di.coreViewInjector
import tech.skot.core.di.get

class Stack : Component<StackView>(), ScreenParent {
    override val view = coreViewInjector.stack()

    var screens: List<Screen<*>> = emptyList()
        set(value) {
            view.screens = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            value.forEach { it.parent = this }
            field = value
        }

    var content: Screen<*>
        get() = screens.last()
        set(value) {
            SKLog.d("Stack will set screens to $value")
            screens = listOf(value)
        }

    override fun push(screen: Screen<*>) {
        SKLog.d("Will push screen: ${screen::class.simpleName}")
        screens += screen

    }

    fun pop(ifRoot:(()->Unit)? = null) {
        if (screens.size>1) {
            screens = screens - screens.last()
        }
        else {
            ifRoot?.invoke()
        }

    }

    override fun remove(screen: Screen<*>) {
        if (screens.contains(screen)) {
            screens = screens - screen
        }
    }

    override fun onRemove() {
        super.onRemove()
        screens.forEach { it.onRemove() }
    }


}