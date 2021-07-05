package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import tech.skot.view.live.MutableSKLiveData


class SKPagerViewProxy(
        override val screens: List<SKScreenViewProxy<*>>,
        override val onSwipeToPage: ((index: Int) -> Unit)?,
        initialSelectedPageIndex:Int,
        override val swipable: Boolean
) : SKComponentViewProxy<ViewPager2>(), SKPagerVC {

    private val selectedPageIndexLD = MutableSKLiveData<Int>(initialSelectedPageIndex)
    override var selectedPageIndex by selectedPageIndexLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: ViewPager2, collectingObservers:Boolean) =
            SKPagerView(this, activity, fragment, binding).apply {
                collectObservers = collectingObservers
                onScreens(screens)
                onOnSwipeToPage(onSwipeToPage)
                onSwipable(swipable)
                selectedPageIndexLD.observe {
                    onSelectedPageIndex(it)
                }
            }


}