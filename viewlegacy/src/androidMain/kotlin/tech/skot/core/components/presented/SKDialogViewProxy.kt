package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData


class SKDialogViewProxy(): SKComponentViewProxy<Unit>(), SKDialogVC {

    private val stateLD = MutableSKLiveData<SKDialogVC.Shown?>(null)

    override var state: SKDialogVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: Unit) =
            SKDialogView(this, activity, fragment).apply {
                stateLD.observe {
                    onState(it)
                }
            }




}