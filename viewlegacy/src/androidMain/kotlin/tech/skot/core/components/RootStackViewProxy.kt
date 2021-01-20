package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.components.presented.BottomSheetViewProxy
import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


class RootStackViewProxy(
        override val bottomSheet: BottomSheetViewProxy) : RootStackView {

    val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenView> = emptyList()
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

    val setRootScreenMessage:SKMessage<ScreenViewProxy<*>> = SKMessage(multiReceiver = false)

//    override fun setRootScreen(aScreen: ScreenView) {
//        setRootScreenMessage.post(aScreen as ScreenViewProxy<*>)
//    }


    override fun onRemove() { }



}