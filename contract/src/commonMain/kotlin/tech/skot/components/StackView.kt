package tech.skot.components

interface StackView: ComponentView {
    fun pushScreen(screen:ScreenView)
    fun popScreen()
    fun clear()
}