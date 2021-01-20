package tech.skot.view.legacy

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.core.components.ScreenView
import tech.skot.core.components.StackView
import tech.skot.view.live.MutableSKLiveData


class StackViewProxy() : ComponentViewProxy<Int>(), StackView {

    private val screensLD: MutableSKLiveData<List<ScreenViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var screens: List<ScreenView>
        get() = screensLD.value
        set(newVal) {
            screensLD.postValue(newVal as List<ScreenViewProxy<*>>)
        }


    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater:LayoutInflater, binding: Int) =
        StackViewImpl(activity, fragment, binding).apply {
            screensLD.observe {
                onScreens(it)
            }
        }




}

class StackViewImpl(activity: SKActivity, fragment: SKFragment?, private val frameLayoutId: Int) : ComponentViewImpl<Int>(activity, fragment, frameLayoutId) {

    fun onScreens(screens: List<ScreenViewProxy<*>>) {

        val lastScreen = screens.lastOrNull()
        if (lastScreen != null) {


            fragmentManager.apply {
                val trans = beginTransaction()
                trans.replace(frameLayoutId, lastScreen.createFragment())
                trans.commit()
            }
        } else {
        }

    }

}