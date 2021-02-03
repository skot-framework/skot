package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class Pager(val screens: List<Screen<*>>, onSwipeToPage: ((index: Int) -> Unit)?, initialSelectedPageIndex: Int = 0) : Component<PagerVC>() {
    override val view = coreViewInjector.pager(screens = screens.map { it.view }, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex)

    var selectedIndex: Int
        get() = view.selectedPageIndex
        set(value) {
            view.selectedPageIndex = value
        }

    override fun onRemove() {
        super.onRemove()
        screens.forEach { it.onRemove() }
    }

}