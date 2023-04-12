package tech.skot.core.components

import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Icon
import tech.skot.core.view.SKSpannedString

class SKPagerWithTabs(
    initialPages: List<TabPage> = emptyList(),
    onUserSwipeToPage: ((index: Int) -> Unit)? = null,
    onUserTabClick: ((index: Int) -> Unit)? = null,
    initialSelectedPageIndex: Int = 0,
    swipable: Boolean = false,
    initialTabsVisibility: SKPagerWithTabsVC.Visibility = SKPagerWithTabsVC.Visibility.Visible
) : SKComponent<SKPagerWithTabsVC>() {
    sealed class TabPage(val screen: SKScreen<*>)
    class Page(screen: SKScreen<*>, val label: String) : TabPage(screen)
    class ConfigurableTabPage(
        screen: SKScreen<*>,
        val tabConfig: TabConfig
    ) : TabPage(screen)

    sealed class TabConfig {
        class CustomTab(val component: SKComponent<*>) : TabConfig()
        class SpannableTitleTab(val title: SKSpannedString) : TabConfig()
        class TitleTab(val title: String) : TabConfig()
        class IconTitleTab(val title: SKSpannedString, val icon: Icon) : TabConfig()
        class IconTab(val icon: Icon) : TabConfig()
    }


    val pager = SKPager(
        initialScreens = initialPages.map { it.screen },
        onUserSwipeToPage = onUserSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )

    var pages: List<TabPage> = initialPages
        set(value) {
            val newComponents = value
                .filterIsInstance<ConfigurableTabPage>().map { it.tabConfig }
                .filterIsInstance<TabConfig.CustomTab>().map { it.component }

            field.filterIsInstance<ConfigurableTabPage>().map { it.tabConfig }
                .filterIsInstance<TabConfig.CustomTab>().forEach {
                    if (!newComponents.contains(it.component)) {
                        it.component.onRemove()
                    }
                }

            pager.screens = value.map { it.screen }
            view.tabConfigs = mapTabConfig(value)
            field = value
        }

    var tabsVisibility: SKPagerWithTabsVC.Visibility = initialTabsVisibility
        set(value) {
            view.tabsVisibility = value
            field = value
        }

    private fun mapTabConfig(values: List<TabPage>): List<SKPagerWithTabsVC.TabConfig> {
        return values.map {
            when (it) {
                is Page -> {
                    SKPagerWithTabsVC.TabConfig.Title(it.label)
                }
                is ConfigurableTabPage -> {
                    when (val tab = it.tabConfig) {
                        is TabConfig.CustomTab -> {
                            SKPagerWithTabsVC.TabConfig.Custom(tab.component.view)
                        }
                        is TabConfig.TitleTab -> {
                            SKPagerWithTabsVC.TabConfig.Title(tab.title)
                        }
                        is TabConfig.IconTitleTab -> {
                            SKPagerWithTabsVC.TabConfig.IconTitle(tab.title, tab.icon)
                        }
                        is TabConfig.IconTab -> {
                            SKPagerWithTabsVC.TabConfig.Icon(tab.icon)
                        }
                        is TabConfig.SpannableTitleTab -> {
                            SKPagerWithTabsVC.TabConfig.SpannableTitle(tab.title)
                        }
                    }
                }
            }
        }
    }

    override val view =
        coreViewInjector.pagerWithTabs(
            pager = pager.view,
            onUserTabClick = onUserTabClick,
            tabConfigs = mapTabConfig(initialPages),
            tabsVisibility = initialTabsVisibility
        )

    override fun onRemove() {
        pager.onRemove()
    }

}