package tech.skot.core.components

import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object SKRootStackViewProxy: SKComponentViewProxy<Unit>(), SKStackVC {

    val screensLD: MutableSKLiveData<List<SKScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<SKScreenVC> = emptyList()
        get() = screensLD.value
        set(newVal) {
            if (!field.isEmpty() && !newVal.isEmpty() && !field.contains(newVal.first())) {
                setRootScreenMessage.post(newVal.first() as SKScreenViewProxy<*>)
            }
            field = newVal
            screensLD.postValue(newVal as List<SKScreenViewProxy<*>>)
        }

    val setRootScreenMessage: SKMessage<SKScreenViewProxy<*>> = SKMessage(multiReceiver = false)

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: Unit, collectingObservers:Boolean): SKComponentView<Unit> {
        throw IllegalAccessException("On ne bind pas la RootStack")
    }

}