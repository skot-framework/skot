package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class SKFrameViewProxy(
        override val screens: Set<SKScreenViewProxy<*>>,
        screenInitial: SKScreenViewProxy<*>?
) : SKComponentViewProxy<FrameLayout>(), SKFrameVC {

    private val screenLD = MutableSKLiveData(screenInitial)
    override var screen: SKScreenVC?
        get() = screenLD.value
        set(value) = screenLD.postValue(value as SKScreenViewProxy<*>)



    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: FrameLayout, collectingObservers: Boolean) =
            SKFrameView(activity, fragment, binding, screens).apply {
                screenLD.observe {
                    onScreen(it)
                }
            }

}