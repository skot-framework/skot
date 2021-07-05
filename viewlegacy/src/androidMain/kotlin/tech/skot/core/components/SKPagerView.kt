package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class SKPagerView(override val proxy:SKPagerViewProxy, activity: SKActivity, fragment: Fragment?, private val viewPager2: ViewPager2) : SKComponentView<ViewPager2>(proxy, activity, fragment, viewPager2) {

    fun onScreens(screens: List<SKScreenViewProxy<*>>) {
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
        //Attention, sans le post on a un pageSelected à 0 lancé au premier affichage
        viewPager2.post {
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    proxy.selectedPageIndex = position
                    onSwipeToPage?.invoke(position)
                }
            })
        }


    }

    fun onSwipable(value:Boolean) {
        viewPager2.isUserInputEnabled = value
    }

}