package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class FrameViewProxy(
        override val screens: Set<ScreenViewProxy<*>>,
        screenInitial: ScreenViewProxy<*>?
) : ComponentViewProxy<FrameLayout>(), FrameVC {

    private val screenLD = MutableSKLiveData(screenInitial)
    override var screen: ScreenVC?
        get() = screenLD.value
        set(value) = screenLD.postValue(value as ScreenViewProxy<*>)



    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: FrameLayout, collectingObservers: Boolean) =
            FrameViewImpl(activity, fragment, binding, screens).apply {
                screenLD.observe {
                    onScreen(it)
                }
            }

}