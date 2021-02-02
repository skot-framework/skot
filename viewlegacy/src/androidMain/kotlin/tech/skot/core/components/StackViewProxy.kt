package tech.skot.core.components

import android.view.LayoutInflater
import tech.skot.view.live.MutableSKLiveData

class StackViewProxy() : ComponentViewProxy<Int>(), StackVC {

    private val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenVC>
        get() = screensLD.value
        set(newVal) {
            screensLD.postValue(newVal as List<ScreenViewProxy<*>>)
        }


    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: Int) =
            StackViewImpl(activity, fragment, binding).apply {
                screensLD.observe {
                    onScreens(it)
                }
            }




}