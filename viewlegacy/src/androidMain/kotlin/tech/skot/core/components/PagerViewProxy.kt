package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import tech.skot.view.live.MutableSKLiveData


class PagerViewProxy(
        override val screens: List<ScreenViewProxy<*>>,
        override val onSwipeToPage: ((index: Int) -> Unit)?,
        initialSelectedPageIndex:Int) : ComponentViewProxy<ViewPager2>(), PagerVC {

    private val selectedPageIndexLD = MutableSKLiveData<Int>(initialSelectedPageIndex)
    override var selectedPageIndex by selectedPageIndexLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: ViewPager2, collectingObservers:Boolean) =
            PagerViewImpl(activity, fragment, binding, this).apply {
                collectObservers = collectingObservers
                onScreens(screens)
                onOnSwipeToPage(onSwipeToPage)
                selectedPageIndexLD.observe {
                    onSelectedPageIndex(it)
                }
            }


}