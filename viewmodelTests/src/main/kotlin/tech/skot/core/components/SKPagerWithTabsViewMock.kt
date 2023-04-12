package tech.skot.core.components

import tech.skot.core.view.Icon
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

class SKPagerWithTabsViewMock(
    pager: SKPagerVC,
    onUserTabClick: ((index: Int) -> Unit)?,
    tabConfigs: List<SKPagerWithTabsVC.TabConfig>,
    tabsVisibility: SKPagerWithTabsVC.Visibility
) : SKComponentViewMock(), SKPagerWithTabsVC {
    override val pager: SKPagerVC = pager
    override var tabConfigs: List<SKPagerWithTabsVC.TabConfig> = tabConfigs
    override var tabsVisibility: SKPagerWithTabsVC.Visibility = tabsVisibility
    override val onUserTabClick = onUserTabClick


}


fun SKPagerWithTabsVC.assertTitle(index: Int, title: String, rule: String = "") {
    when (val tab = this.tabConfigs[index]) {
        is SKPagerWithTabsVC.TabConfig.Custom -> {
            fail("$rule -> this tab is Custom")
        }
        is SKPagerWithTabsVC.TabConfig.Icon -> {
            fail("$rule -> no title")
        }
        is SKPagerWithTabsVC.TabConfig.IconTitle -> {
            assertEquals(
                expected = title,
                actual = tab.title.joinToString("") { it.text },
                message = "$rule -> wrong tab title"
            )
        }
        is SKPagerWithTabsVC.TabConfig.SpannableTitle -> {
            assertEquals(
                expected = title,
                actual = tab.title.joinToString("") { it.text },
                message = "$rule -> wrong tab title"
            )
        }
        is SKPagerWithTabsVC.TabConfig.Title -> {
            assertEquals(
                expected = title,
                actual = tab.title,
                message = "$rule -> wrong tab title"
            )
        }
    }
}

fun SKPagerWithTabsVC.assertIcon(index: Int, icon: Icon, rule: String = "") {
    when (val tab = this.tabConfigs[index]) {
        is SKPagerWithTabsVC.TabConfig.Custom -> {
            fail("$rule -> this tab is Custom")
        }
        is SKPagerWithTabsVC.TabConfig.Icon -> {
            assertEquals(
                expected = icon,
                actual = tab.icon,
                message = "$rule -> wrong tab icon"
            )
        }
        is SKPagerWithTabsVC.TabConfig.IconTitle -> {
            assertEquals(
                expected = icon,
                actual = tab.icon,
                message = "$rule -> wrong tab icon"
            )
        }
        is SKPagerWithTabsVC.TabConfig.SpannableTitle -> {
            fail("$rule -> no icon")
        }
        is SKPagerWithTabsVC.TabConfig.Title -> {
            fail("$rule -> no icon")
        }
    }
}

inline fun <reified T : SKComponentVC> SKPagerWithTabsVC.assertTabComponentInstanceOf(index: Int, rule: String = ""){
    when (val tab = this.tabConfigs[index]) {
        is SKPagerWithTabsVC.TabConfig.Custom -> {
            assertIs<T>(tab.tab, message = "$rule wrong SKComponent type")
        }
        is SKPagerWithTabsVC.TabConfig.Icon -> {
            fail("$rule -> this tab is not Custom")
        }
        is SKPagerWithTabsVC.TabConfig.IconTitle -> {
            fail("$rule -> this tab is not Custom")
        }
        is SKPagerWithTabsVC.TabConfig.SpannableTitle -> {
            fail("$rule -> this tab is not Custom")
        }
        is SKPagerWithTabsVC.TabConfig.Title -> {
            fail("$rule -> this tab is not Custom")
        }
    }
}