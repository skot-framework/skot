package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class PagerViewImpl(activity: SKActivity, fragment: SKFragment?, private val viewPager2: ViewPager2, private val proxy: PagerViewProxy) : ComponentViewImpl<ViewPager2>(activity, fragment, viewPager2) {

    fun onScreens(screens: List<ScreenViewProxy<*>>) {
        viewPager2.adapter = object : FragmentStateAdapter(fragmentManager, lifecycle) {
            override fun getItemCount() = screens.size

            override fun createFragment(position: Int): Fragment {
                return screens[position].createFragment()
            }
        }
    }

    fun onSelectedPageIndex(selectedPageIndex: Int) {
        viewPager2.post {
            viewPager2.currentItem = selectedPageIndex
        }
    }

    fun onOnSwipeToPage(onSwipeToPage: ((index: Int) -> Unit)?) {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                proxy.selectedPageIndex = position
                onSwipeToPage?.invoke(position)
            }
        })
    }

}