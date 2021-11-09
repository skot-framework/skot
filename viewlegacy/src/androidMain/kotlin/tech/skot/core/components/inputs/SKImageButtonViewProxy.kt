package tech.skot.core.components.inputs


import android.widget.ImageButton
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.view.Icon
import tech.skot.view.live.MutableSKLiveData

class SKImageButtonViewProxy(
    onTapInitial:(()->Unit)?,
    inconInitial:Icon,
    enabledInitial:Boolean?,
    hiddenInitial:Boolean?
): SKComponentViewProxy<ImageButton>(), SKImageButtonVC {

    private val onTapLD = MutableSKLiveData(onTapInitial)
    override var onTap: (() -> Unit)? by onTapLD

    private val iconLD = MutableSKLiveData(inconInitial)
    override var icon by iconLD

    private val enabledLD = MutableSKLiveData(enabledInitial)
    override var enabled by enabledLD

    private val hiddenLD = MutableSKLiveData(hiddenInitial)
    override var hidden by hiddenLD

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: ImageButton
    ) = SKImageButtonView(this, activity, fragment, binding).apply {
        onTapLD.observe {
            onOnTap(it)
        }
        iconLD.observe {
            onIcon(it)
        }
        enabledLD.observe {
            onEnabled(it)
        }
        hiddenLD.observe {
            onHidden(it)
        }
    }

}
