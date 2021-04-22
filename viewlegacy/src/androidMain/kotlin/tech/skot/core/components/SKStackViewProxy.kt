package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class SKStackViewProxy() : SKComponentViewProxy<FrameLayout>(), SKStackVC {

    private val screensLD: MutableSKLiveData<List<SKScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<SKScreenVC>
        get() = screensLD.value
        set(newVal) {
            val newProxyList = newVal as List<SKScreenViewProxy<*>>
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
            SKStackView(activity, fragment, binding).apply {
                collectObservers = collectingObservers
                screensLD.observe {
                    onScreens(it)
                }
            }


}