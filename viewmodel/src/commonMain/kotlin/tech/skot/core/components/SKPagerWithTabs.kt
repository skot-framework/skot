package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKPagerWithTabs(
    initialPages: List<Page> = emptyList(),
    onSwipeToPage: ((index: Int) -> Unit)? = null,
    initialSelectedPageIndex: Int = 0,
    swipable: Boolean = false
):SKComponent<SKPagerWithTabsVC>() {
    class Page(val screen:SKScreen<*>, val label:String)


    val pager = SKPager(
        initialScreens = initialPages.map { it.screen },
        onSwipeToPage = onSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )

    var pages: List<Page> = initialPages
        set(value) {
            pager.screens = value.map { it.screen }
            view.labels = value.map { it.label }
            field = value
        }

    override val view = coreViewInjector.pagerWithTabs(pager.view, initialPages.map { it.label })

    override fun onRemove() {
        pager.onRemove()
    }

}