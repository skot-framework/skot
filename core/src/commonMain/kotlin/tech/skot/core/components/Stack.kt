package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.di.get

class Stack : Component<StackView>() {
    override val view = get<StackView>()

    var screens: List<Screen<*>> = emptyList()
        set(value) {
            field = value
            view.screens = value.map { it.view }
            SKLog.d("Stack view.screens just set to ${view.screens}")

        }

    var content: Screen<*>
        get() = screens.last()
        set(value) {
            SKLog.d("Stack will set screens to $value")
            screens = listOf(value)
        }

    fun push(screen: Screen<*>) {
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


}