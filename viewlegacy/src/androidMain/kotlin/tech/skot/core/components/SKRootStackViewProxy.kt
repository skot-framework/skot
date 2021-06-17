package tech.skot.core.components

import androidx.fragment.app.Fragment
import tech.skot.view.SKTransitionAndroidLegacy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object SKRootStackViewProxy : SKComponentViewProxy<Unit>(), SKStackVC {

    val stateLD: MutableSKLiveData<StateProxy> =
        MutableSKLiveData(StateProxy(emptyList(), null))

    override var state: SKStackVC.State = StateProxy(screens = emptyList(), transition = null)
        set(newVal) {
            val newProxyList = newVal.screens as List<SKScreenViewProxy<*>>


            if (!field.screens.isEmpty() && !newProxyList.isEmpty() && !field.screens.contains(
                    newProxyList.first()
                )
            ) {
                setRootScreenMessage.post(newProxyList.first())
            }
            field = newVal
            stateLD.postValue(
                StateProxy(
                    screens = newProxyList,
                    transition = newVal.transition as SKTransitionAndroidLegacy?
                )
            )
        }


    val setRootScreenMessage: SKMessage<SKScreenViewProxy<*>> = SKMessage(multiReceiver = false)

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: Unit,
        collectingObservers: Boolean
    ): SKComponentView<Unit> {
        throw IllegalAccessException("On ne bind pas la RootStack")
    }

}