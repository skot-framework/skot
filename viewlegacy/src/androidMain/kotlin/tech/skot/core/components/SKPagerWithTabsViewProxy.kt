package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import tech.skot.viewlegacy.databinding.SkPagerWithTabsBinding

class SKPagerWithTabsViewProxy(
    override val pager: SKPagerViewProxy,
    override val labels: List<String>,
) : SKComponentViewProxy<View>(), SKPagerWithTabsVC {

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: View,
        collectingObservers: Boolean
    ) :SKPagerWithTabsView {
        val pagerView = binding.findViewWithTag<ViewPager2>("sk_view_pager2")
        val tabLayoutView = binding.findViewWithTag<TabLayout>("sk_tab_layout")
        return SKPagerWithTabsView(activity, fragment, binding, pagerView, tabLayoutView).apply {
            collectObservers = collectingObservers
            pager.bindTo(activity, fragment, pagerView, collectingObservers)
            onLabels(labels)
        }
    }

}