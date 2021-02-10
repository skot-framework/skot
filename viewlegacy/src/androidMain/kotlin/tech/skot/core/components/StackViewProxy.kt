package tech.skot.core.components

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class StackViewProxy() : ComponentViewProxy<Int>(), StackVC {

    private val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenVC>
        get() = screensLD.value
        set(newVal) {
            val newProxyList = newVal as List<ScreenViewProxy<*>>
            screensLD.value.lastOrNull()?.let {
                if (newProxyList.lastOrNull() != it && newProxyList.contains(it)) {
                    it.saveState()
                }
            }
            screensLD.postValue(newProxyList)
        }


    override fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, binding: Int) =
            StackViewImpl(activity, fragment, binding).apply {
                screensLD.observe {
                    onScreens(it)
                }
            }




}