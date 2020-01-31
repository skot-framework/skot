package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import tech.skot.core.SKLog
import tech.skot.view.live.MutableSKLiveData

open class PagerViewImpl(override val screens: List<ScreenView>, selectedPageIndex:Int) : ComponentViewImpl<AppCompatActivity, Fragment, ViewPager2>(), PagerView {

    lateinit var viewPager: ViewPager2

    override fun onInflated() {
        super.onInflated()
        viewPager = binding
        viewPager.adapter = object : FragmentStateAdapter(fragmentManager, lifeCycleOwner.lifecycle) {
            override fun getItemCount() = screens.size

            override fun createFragment(position: Int): Fragment {
                val screen = screens[position]
                val screenViewImpl = ScreenViewImpl.getInstance(screen.key)
                if (screenViewImpl != null) {
                    return screenViewImpl.createFragmentWithKey()
                } else {
                    throw IllegalStateException("No View for key ${screen.key}").apply {
                        SKLog.e("PagerViewImpl -> No View for key ${screen.key}", this)
                    }
                }
            }

        }
    }


    private val selectedPageIndexLD: MutableSKLiveData<Int> = MutableSKLiveData(selectedPageIndex)

    override var selectedPageIndex: Int
        get() = selectedPageIndexLD.value
        set(newVal) {
            selectedPageIndexLD.postValue(newVal)
        }

    fun onSelectedPageIndex(selectedPageIndex: Int) {
        viewPager.post {
            viewPager.currentItem = selectedPageIndex
        }

    }

    override fun linkTo(lifecycleOwner: LifecycleOwner) {
        super.linkTo(lifecycleOwner)
        selectedPageIndexLD.observe(lifecycleOwner) {
            onSelectedPageIndex(it)
        }
    }

}