package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKPagerWithTabs(
    pages: List<Page>,
    onSwipeToPage: ((index: Int) -> Unit)? = null,
    initialSelectedPageIndex: Int = 0,
    swipable: Boolean = false
):SKComponent<SKPagerWithTabsVC>() {
    class Page(val screen:SKScreen<*>, val label:String)

    val pager = SKPager(
        screens = pages.map { it.screen },
        onSwipeToPage = onSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )

    override val view = coreViewInjector.pagerWithTabs(pager.view, pages.map { it.label })

    override fun onRemove() {
        pager.onRemove()
    }

}