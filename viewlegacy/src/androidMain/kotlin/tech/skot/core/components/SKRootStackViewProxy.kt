package tech.skot.core.components

import androidx.fragment.app.Fragment
import tech.skot.view.SKTransitionAndroidLegacy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object SKRootStackViewProxy : SKComponentViewProxy<Unit>(), SKStackVC {

    data class StateRootProxy(val state: StateProxy, val screenKeyNeedToOpenRoot: Long?)

    val stateLD: MutableSKLiveData<StateRootProxy> =
        MutableSKLiveData(StateRootProxy(StateProxy(emptyList(), null), null))

    override var state: SKStackVC.State = StateProxy(screens = emptyList(), transition = null)
        set(newVal) {
            val newProxyList = newVal.screens as List<SKScreenViewProxy<*>>


            val oldRootNeedingToOpenNewOne =
                if (!field.screens.isEmpty() && !newProxyList.isEmpty() && !field.screens.contains(
                        newProxyList.first()
                    )
                ) {
                    (field.screens.firstOrNull() as? SKScreenViewProxy<*>)?.key
                } else {
                    null
                }

            field = newVal
            stateLD.postValue(
                StateRootProxy(
                    StateProxy(
                        screens = newProxyList,
                        transition = newVal.transition as SKTransitionAndroidLegacy?
                    ), oldRootNeedingToOpenNewOne
                )
            )
        }



    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: Unit
    ): SKComponentView<Unit> {
        throw IllegalAccessException("On ne bind pas la RootStack")
    }


}