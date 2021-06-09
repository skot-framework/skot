package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKPager(val screens: List<SKScreen<*>>, onSwipeToPage: ((index: Int) -> Unit)? = null, initialSelectedPageIndex: Int = 0, swipable:Boolean = true) : SKComponent<SKPagerVC>() {
    override val view = coreViewInjector.pager(screens = screens.map { it.view }, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex, swipable = swipable)

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