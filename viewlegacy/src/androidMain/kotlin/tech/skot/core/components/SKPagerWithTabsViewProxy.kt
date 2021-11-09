package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import tech.skot.view.live.MutableSKLiveData

class SKPagerWithTabsViewProxy(
    override val pager: SKPagerViewProxy,
    initialLabels: List<String>,
) : SKComponentViewProxy<View>(), SKPagerWithTabsVC {

    private val labelsLD = MutableSKLiveData(initialLabels)
    override var labels : List<String>
        get() = labelsLD.value
        set(value) {
            labelsLD.postValue(value)
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
            labelsLD.observe {
                onLabels(labels)
            }

        }
    }

}