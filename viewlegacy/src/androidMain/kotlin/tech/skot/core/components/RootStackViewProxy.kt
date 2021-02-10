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
            if (!field.isEmpty() && !field.contains(newVal.first())) {
                SKLog.d("----will send setRootScreenMessage  ${newVal.first()}")
                setRootScreenMessage.post(newVal.first() as ScreenViewProxy<*>)
                //RootStack.view.setRootScreen(newVal.first())
            }
            field = newVal
            SKLog.d("----will send screensLD  ${newVal}")
            screensLD.postValue(newVal as List<ScreenViewProxy<*>>)
        }

    val setRootScreenMessage: SKMessage<ScreenViewProxy<*>> = SKMessage(multiReceiver = false)

    override fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, binding: Unit): ComponentViewImpl<Unit> {
        throw IllegalAccessException("On ne bind pas la RootStack")
    }

}