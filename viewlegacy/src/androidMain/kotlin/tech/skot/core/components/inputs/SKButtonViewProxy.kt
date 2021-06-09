package tech.skot.core.components.inputs

import android.widget.Button
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData

class SKButtonViewProxy(
    onTapInitial:(()->Unit)?,
    labelInitial:String?,
    enabledInitial:Boolean?,
    hiddenInitial:Boolean?
): SKComponentViewProxy<Button>(), SKButtonVC {

    private val onTapLD = MutableSKLiveData(onTapInitial)
    override var onTap: (() -> Unit)? by onTapLD

    private val labelLD = MutableSKLiveData(labelInitial)
    override var label by labelLD

    private val enabledLD = MutableSKLiveData(enabledInitial)
    override var enabled by enabledLD

    private val hiddenLD = MutableSKLiveData(hiddenInitial)
    override var hidden by hiddenLD

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: Button,
        collectingObservers: Boolean
    ) = SKButtonView(activity, fragment, binding).apply {
        collectObservers = collectingObservers
        onTapLD.observe {
            onOnTap(it)
        }
        labelLD.observe {
            onLabel(it)
        }
        enabledLD.observe {
            onEnabled(it)
        }
        hiddenLD.observe {
            onHidden(it)
        }
    }

}