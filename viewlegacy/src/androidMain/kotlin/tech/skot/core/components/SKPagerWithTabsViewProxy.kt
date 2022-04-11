package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import tech.skot.view.live.MutableSKLiveData

class SKPagerWithTabsViewProxy(
    override val pager: SKPagerViewProxy,
    initialTabConfigs: List<SKPagerWithTabsVC.TabConfig>,
    initialTabsVisibility: SKPagerWithTabsVC.Visibility,
) : SKComponentViewProxy<View>(), SKPagerWithTabsVC {

    private val tabConfigsLD = MutableSKLiveData(initialTabConfigs)
    override var tabConfigs : List<SKPagerWithTabsVC.TabConfig>
        get() = tabConfigsLD.value
        set(value) {
            tabConfigsLD.postValue(value)
        }

    private val tabsVisibilityLD = MutableSKLiveData(initialTabsVisibility)
    override var tabsVisibility : SKPagerWithTabsVC.Visibility
        get() = tabsVisibilityLD.value
        set(value) {
            tabsVisibilityLD.postValue(value)
        }

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: View
    ) :SKPagerWithTabsView {
        val pagerView = binding.findViewWithTag<ViewPager2>("sk_view_pager2")
        val tabLayoutView = binding.findViewWithTag<TabLayout>("sk_tab_layout")
        return SKPagerWithTabsView(this, activity, fragment, binding, pagerView, tabLayoutView).apply {
            pager.bindTo(activity, fragment, pagerView)
            tabConfigsLD.observe {
                onLabels(it)
            }
            tabsVisibilityLD.observe {
                onShowTabs(it)
            }

        }
    }

}