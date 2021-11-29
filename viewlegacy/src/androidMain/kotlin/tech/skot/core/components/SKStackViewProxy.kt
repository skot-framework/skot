package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.SKTransitionAndroidLegacy
import tech.skot.view.live.MutableSKLiveData

data class StateProxy(override val screens:List<SKScreenViewProxy<*>>, override val transition:SKTransitionAndroidLegacy?):SKStackVC.State(screens, transition)

class SKStackViewProxy() : SKComponentViewProxy<FrameLayout>(), SKStackVC {

    private val stateLD: MutableSKLiveData<StateProxy> = MutableSKLiveData(StateProxy(emptyList(),null))

    override var state: SKStackVC.State
        get() = stateLD.value
        set(newVal) {
            val newProxyList = newVal.screens as List<SKScreenViewProxy<*>>
            stateLD.value.screens.lastOrNull()?.let {
                if (newProxyList.lastOrNull() != it && newProxyList.contains(it)) {
                    it.saveState()
                }
            }
            stateLD.postValue(StateProxy(newProxyList, newVal.transition as SKTransitionAndroidLegacy?))
        }

    override fun saveState() {
        for (screenViewProxy in stateLD.value.screens) {
            screenViewProxy.saveState()
        }
    }

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: FrameLayout) =
            SKStackView(this, activity, fragment, binding).apply {
                stateLD.observe {
                    onState(it)
                }
            }



}