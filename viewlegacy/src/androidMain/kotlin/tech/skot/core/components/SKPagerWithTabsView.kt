package tech.skot.core.components

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import tech.skot.core.view.toCharSequence
import tech.skot.view.extensions.setVisible

class SKPagerWithTabsView(
    override val proxy: SKPagerWithTabsViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    view: View,
    private val viewPager2: ViewPager2,
    private val tabLayout: TabLayout
) : SKComponentView<View>(proxy, activity, fragment, view) {
    private var tabLayoutMediator : TabLayoutMediator? = null

    fun onLabels(tabConfigs: List<SKPagerWithTabsVC.TabConfig>) {
        tabLayoutMediator?.takeIf { it.isAttached }?.detach()
        tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, index ->
            tab.view.setOnTouchListener { v, event ->
                if(!tab.isSelected) {
                    proxy.onUserTabClick?.invoke(tab.position)
                    proxy.pager.selectedPageIndex = tab.position
                }
                false
            }
            when (val tabConfig = tabConfigs.get(index)) {
                is SKPagerWithTabsVC.TabConfig.Icon -> {
                    tab.setIcon(tabConfig.icon.res)
                    tab.tabLabelVisibility = TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
                }
                is SKPagerWithTabsVC.TabConfig.IconTitle -> {
                    tab.text = tabConfig.title.toCharSequence(context)
                    tab.setIcon(tabConfig.icon.res)

                }
                is SKPagerWithTabsVC.TabConfig.Title -> {
                    tab.text = tabConfig.title
                }
                is SKPagerWithTabsVC.TabConfig.Custom -> {
                    (tabConfig.tab as SKComponentViewProxy<*>).layoutId?.let {
                        LayoutInflater.from(context).inflate(it, tab.view, false)
                    }?.let {
                        (tabConfig.tab as SKComponentViewProxy<*>).bindToItemView(
                            activity,
                            fragment,
                            it
                        )
                        tab.setCustomView(it)
                    }
                }
                is SKPagerWithTabsVC.TabConfig.SpannableTitle -> {
                    tab.text = tabConfig.title.toCharSequence(context)
                }
            }
        }.apply {
            this.attach()
        }
        automaticShowTabs()
    }

    override fun onRecycle() {
        super.onRecycle()
    }

    private fun automaticShowTabs() {
        if (viewPager2.adapter?.itemCount ?: 0 > 1) {
            tabLayout.setVisible(true)
        } else {
            tabLayout.setVisible(false)
        }
    }

    fun onShowTabs(tabsVisibility: SKPagerWithTabsVC.Visibility) {
        when (tabsVisibility) {
            SKPagerWithTabsVC.Visibility.Automatic -> {
                automaticShowTabs()
            }
            SKPagerWithTabsVC.Visibility.Gone -> tabLayout.setVisible(false)
            SKPagerWithTabsVC.Visibility.Visible -> tabLayout.setVisible(true)
        }

    }
}