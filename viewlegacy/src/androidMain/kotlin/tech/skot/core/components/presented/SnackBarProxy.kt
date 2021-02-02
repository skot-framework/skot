package tech.skot.core.components.presented

import android.view.LayoutInflater
import android.view.View
import tech.skot.core.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class SnackBarViewProxy() : ComponentViewProxy<View>(), SnackBarVC {

    private val stateLD = MutableSKLiveData<SnackBarVC.Shown?>(null)

    override var state: SnackBarVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: View) =
            SnackBarViewImpl(activity, fragment, binding, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }

}