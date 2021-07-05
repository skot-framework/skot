package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SKPagerWithTabsView(
    override val proxy: SKPagerWithTabsViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    view: View,
    private val viewPager2: ViewPager2,
    private val tabLayout: TabLayout
) : SKComponentView<View>(proxy, activity, fragment, view) {

    fun onLabels(labels: List<String>) {
        TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, index ->
            tab.text = labels.get(index)
        }.attach()
    }

}