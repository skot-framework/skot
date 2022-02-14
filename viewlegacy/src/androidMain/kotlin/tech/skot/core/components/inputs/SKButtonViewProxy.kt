package tech.skot.core.components.inputs

import android.widget.Button
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R

class SKButtonViewProxy(
    onTapInitial:(()->Unit)? = null,
    labelInitial:String? = null,
    enabledInitial:Boolean? = null,
    hiddenInitial:Boolean? = null
): SKComponentViewProxy<Button>(), SKButtonVC {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_button

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
        binding: Button
    ) = SKButtonView(this, activity, fragment, binding).apply {
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