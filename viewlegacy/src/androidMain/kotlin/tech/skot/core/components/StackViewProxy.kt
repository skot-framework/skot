package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class StackViewProxy() : ComponentViewProxy<FrameLayout>(), StackVC {

    private val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenVC>
        get() = screensLD.value
        set(newVal) {
            val newProxyList = newVal as List<ScreenViewProxy<*>>
            screensLD.value.lastOrNull()?.let {
                if (newProxyList.lastOrNull() != it && newProxyList.contains(it)) {
                    it.saveState()
                }
            }
            screensLD.postValue(newProxyList)
        }

    override fun saveState() {
        for (screenViewProxy in screensLD.value) {
            screenViewProxy.saveState()
        }
    }

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: FrameLayout, collectingObservers: Boolean) =
            StackView(activity, fragment, binding).apply {
                collectObservers = collectingObservers
                screensLD.observe {
                    onScreens(it)
                }
            }


}