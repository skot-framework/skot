package tech.skot.components

abstract class Stack<V : StackView> : Component<V>(), ScreenParent {

    private val _screens = mutableListOf<Screen<*>>()
    val screens: List<Screen<*>>
        get() = _screens

    fun push(screen: Screen<*>) {
        _screens.add(screen)
        screen._parent = this
        view.pushScreen(screen.view)
    }


    fun popScreen() {
        _screens.remove(_screens.last())
        view.popScreen()
    }

    fun clear() {
        _screens.forEach { it.onRemove() }
        _screens.clear()
        view.clear()
    }


    override fun remove(aScreen: Screen<*>) {
        if (_screens.lastOrNull() == aScreen) {
            popScreen()
            aScreen.onRemove()
        }
    }

    override fun onRemove() {
        _screens.forEach {
            it.onRemove()
        }
        super.onRemove()

    }
}