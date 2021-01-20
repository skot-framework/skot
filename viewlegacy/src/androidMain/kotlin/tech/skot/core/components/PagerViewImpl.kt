package tech.skot.core.components

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData


class PagerViewProxy(
        override val screens: List<ScreenViewProxy<*>>,
        override val onSwipeToPage: ((index: Int) -> Unit)?,
        initialSelectedPageIndex:Int) : ComponentViewProxy<ViewPager2>(), PagerView {

    private val selectedPageIndexLD = MutableSKLiveData<Int>(initialSelectedPageIndex)
    override var selectedPageIndex by selectedPageIndexLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, viewPager2: ViewPager2) =
            PagerViewImpl(activity, fragment, viewPager2, this).apply {
                onScreens(screens)
                onOnSwipeToPage(onSwipeToPage)
                selectedPageIndexLD.observe {
                    onSelectedPageIndex(it)
                }
            }


}

class PagerViewImpl(activity: SKActivity, fragment: SKFragment?, private val viewPager2: ViewPager2, private val proxy:PagerViewProxy) : ComponentViewImpl<ViewPager2>(activity, fragment, viewPager2) {

    fun onScreens(screens: List<ScreenViewProxy<*>>) {
        viewPager2.adapter = object : FragmentStateAdapter(fragmentManager, lifecycle) {
            override fun getItemCount() = screens.size

            override fun createFragment(position: Int): Fragment {
                return screens[position].createFragment()
            }
        }
    }

    fun onSelectedPageIndex(selectedPageIndex:Int) {
        viewPager2.post {
            viewPager2.currentItem = selectedPageIndex
        }
    }

    fun onOnSwipeToPage(onSwipeToPage:((index:Int)->Unit)?) {
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                proxy.selectedPageIndex = position
                onSwipeToPage?.invoke(position)
            }
        })
    }

}