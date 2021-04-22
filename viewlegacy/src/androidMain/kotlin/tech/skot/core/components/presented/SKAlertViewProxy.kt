package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData

class SKAlertViewProxy() : SKComponentViewProxy<Unit>(), SKAlertVC {

    private val stateLD = MutableSKLiveData<SKAlertVC.Shown?>(null)

    override var state: SKAlertVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: Unit, collectingObservers:Boolean) =
            SKAlertView(activity, fragment, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }


}