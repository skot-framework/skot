package tech.skot.view.legacy

import tech.skot.contract.view.RootStackView
import tech.skot.contract.view.ScreenView
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewmodel.RootStack


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

    val setRootScreenMessage:SKMessage<ScreenViewProxy<*>> = SKMessage()

//    override fun setRootScreen(aScreen: ScreenView) {
//        setRootScreenMessage.post(aScreen as ScreenViewProxy<*>)
//    }


    override fun onRemove() { }



}