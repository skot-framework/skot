package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKPager(
    initialScreens: List<SKScreen<*>>,
    onUserSwipeToPage: ((index: Int) -> Unit)? = null,
    initialSelectedPageIndex: Int = 0,
    swipable: Boolean = true,
) : SKComponent<SKPagerVC>() {


    override val view = coreViewInjector.pager(
        screens = initialScreens.map { it.view },
        onUserSwipeToPage = onUserSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )

    var selectedIndex: Int
        get() = view.selectedPageIndex
        set(value) {
            view.selectedPageIndex = value
        }

    var screens: List<SKScreen<*>> = initialScreens
        set(value) {
            view.screens = value.map { it.view }
            field = value
        }

    override fun onRemove() {
        super.onRemove()
        screens.forEach { it.onRemove() }
    }

}