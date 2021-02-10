package tech.skot.core.components.presented

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import tech.skot.core.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class AlertViewProxy() : ComponentViewProxy<Unit>(), AlertVC {

    private val stateLD = MutableSKLiveData<AlertVC.Shown?>(null)

    override var state: AlertVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, binding: Unit) =
            AlertViewImpl(activity, fragment, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }


}