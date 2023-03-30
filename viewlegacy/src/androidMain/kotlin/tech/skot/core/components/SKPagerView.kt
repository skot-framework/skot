package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class SKPagerView(
    override val proxy: SKPagerViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val viewPager2: ViewPager2,
) : SKComponentView<ViewPager2>(proxy, activity, fragment, viewPager2) {

    fun onScreens(screens: List<SKScreenViewProxy<*>>) {
        viewPager2.adapter =
            object : FragmentStateAdapter(fragmentManager, lifecycleOwner.lifecycle) {
                override fun getItemCount() = screens.size

                override fun createFragment(position: Int): Fragment {
                    return screens[position].createFragment(canSetFullScreen = false)
                }
            }
    }


    fun onSelectedPageIndex(selectedPageIndex: Int) {
        viewPager2.post {
            viewPager2.currentItem = selectedPageIndex
        }
    }


    private var onUserSwipeCallBack: ViewPager2.OnPageChangeCallback? = null
    fun onOnUserSwipeToPage(onSwipeToPage: ((index: Int) -> Unit)?) {
        var userSwipe: Boolean = false
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> userSwipe = true
                    ViewPager2.SCROLL_STATE_IDLE -> userSwipe = false
                }
            }

            override fun onPageSelected(position: Int) {
                if (userSwipe) {
                    proxy.selectedPageIndex = position
                    onSwipeToPage?.invoke(position)
                }
            }
        }.also { onUserSwipeCallBack = it })

    }

    init {
        lifecycleOwner.skLifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                unregisterCallBack()
            }
        })
    }

    private fun unregisterCallBack() {
        onUserSwipeCallBack?.let {
            viewPager2.unregisterOnPageChangeCallback(it)
            onUserSwipeCallBack = null
        }
    }

    override fun onRecycle() {
        super.onRecycle()
        unregisterCallBack()
    }

    fun onSwipable(value: Boolean) {
        viewPager2.isUserInputEnabled = value
    }

}