package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData

class SKWindowPopupViewProxy() : SKComponentViewProxy<Unit>(), SKWindowPopupVC {

    private val stateLD = MutableSKLiveData<SKWindowPopupVC.Shown?>(null)

    override var state: SKWindowPopupVC.Shown? by stateLD

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: Unit,
        collectingObservers: Boolean
    ) =
        SKWindowPopupView(this, activity, fragment).apply {
            stateLD.observe {
                onState(it)
            }
        }

}