package tech.skot.core.components

import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object RootStackViewProxy : RootStackView {

    val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenView> = emptyList()
        get() = screensLD.value
        set(newVal) {
            if (!field.isEmpty() && !field.contains(newVal.first())) {
                setRootScreenMessage.post(newVal.first() as ScreenViewProxy<*>)
                //RootStack.view.setRootScreen(newVal.first())
            }
            field = newVal
            screensLD.postValue(newVal as List<ScreenViewProxy<*>>)
        }

    val setRootScreenMessage:SKMessage<ScreenViewProxy<*>> = SKMessage(multiReceiver = false)

//    override fun setRootScreen(aScreen: ScreenView) {
//        setRootScreenMessage.post(aScreen as ScreenViewProxy<*>)
//    }


    override fun onRemove() { }



}