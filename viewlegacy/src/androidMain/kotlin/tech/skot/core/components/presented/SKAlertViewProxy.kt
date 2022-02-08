package tech.skot.core.components.presented

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData

class SKAlertViewProxy() : SKComponentViewProxy<Unit>(), SKAlertVC {

    private val stateLD = MutableSKLiveData<SKAlertVC.Shown?>(null)

    override var state: SKAlertVC.Shown? by stateLD

    private val inputTextLD =  MutableSKLiveData<String?>(null)

    override var inputText: String? by inputTextLD

    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) {
//        super.inflate(layoutInflater, parent, attachToParent)
    }

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: Unit) =
            SKAlertView(this, activity, fragment).apply {
                stateLD.observe {
                    onState(it)
                }
                inputTextLD.observe {
                    onInputText(it)
                }
            }


}