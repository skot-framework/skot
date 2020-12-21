package tech.skot.core.components

import tech.skot.core.di.get

object RootStack {
    val view = get<RootStackView>()

    var screens: List<Screen<*>> = emptyList()
        set(value) {
            view.screens = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value

        }

    var content: Screen<*>
        get() = screens.last()
        set(value) {
            screens = listOf(value)
        }

    fun push(screen: Screen<*>) {
        screens += screen
    }

    fun pop() {
        screens = screens - screens.last()
    }
}