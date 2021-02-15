package tech.skot.core.components

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object RootStackViewProxy: ComponentViewProxy<Unit>(), StackVC {

    val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenVC> = emptyList()
        get() = screensLD.value
        set(newVal) {
            if (!field.isEmpty() && !newVal.isEmpty() && !field.contains(newVal.first())) {
                setRootScreenMessage.post(newVal.first() as ScreenViewProxy<*>)
            }
            field = newVal
            screensLD.postValue(newVal as List<ScreenViewProxy<*>>)
        }

    val setRootScreenMessage: SKMessage<ScreenViewProxy<*>> = SKMessage(multiReceiver = false)

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: Unit, collectingObservers:Boolean): ComponentViewImpl<Unit> {
        throw IllegalAccessException("On ne bind pas la RootStack")
    }

}