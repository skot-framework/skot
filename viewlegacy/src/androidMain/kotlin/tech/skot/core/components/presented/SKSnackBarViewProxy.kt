package tech.skot.core.components.presented

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData

class SKSnackBarViewProxy() : SKComponentViewProxy<View>(), SKSnackBarVC {

    private val stateLD = MutableSKLiveData<SKSnackBarVC.Shown?>(null)

    override var state: SKSnackBarVC.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: View, collectingObservers:Boolean) =
            SKSnackBarView(this, activity, fragment, binding).apply {
                stateLD.observe {
                    onState(it)
                }
            }

}