package tech.skot.core.components.presented

import android.view.LayoutInflater
import tech.skot.core.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData


class BottomSheetViewProxy(): ComponentViewProxy<Unit>(), BottomSheetVC {

    private val stateLD = MutableSKLiveData<BottomSheetVC.Shown?>(null)

    override var state: BottomSheetVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: Unit) =
            BottomSheetViewImpl(activity, fragment, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }




}