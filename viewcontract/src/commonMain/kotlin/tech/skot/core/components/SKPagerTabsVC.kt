package tech.skot.core.components

import tech.skot.core.view.SKSpannedString

@SKLayoutIsSimpleView
interface SKPagerWithTabsVC : SKComponentVC {
    val pager: SKPagerVC
    var tabConfigs: List<TabConfig>
    var tabsVisibility: Visibility

    sealed class TabConfig {
        class IconTitle(val title: SKSpannedString, val icon: tech.skot.core.view.Icon) : TabConfig()
        class Title(val title: String) : TabConfig()
        class SpannableTitle(val title: SKSpannedString) : TabConfig()
        class Icon(val icon: tech.skot.core.view.Icon) : TabConfig()
        class Custom(val tab: SKComponentVC) : TabConfig()
    }

    sealed class Visibility {
        object Visible : Visibility()
        object Gone : Visibility()
        object Automatic : Visibility()
    }

}