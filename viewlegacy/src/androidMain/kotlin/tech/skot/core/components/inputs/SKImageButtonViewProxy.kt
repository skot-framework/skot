package tech.skot.core.components.inputs


import android.widget.ImageButton
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.view.Icon
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R

class SKImageButtonViewProxy(
    onTapInitial: (() -> Unit)?,
    iconInitial: Icon,
    enabledInitial: Boolean? = true,
    hiddenInitial: Boolean? = false,
    override val debounce: Long? = 500
) : SKComponentViewProxy<ImageButton>(), SKImageButtonVC {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_image_button

    private val onTapLD = MutableSKLiveData(onTapInitial)
    override var onTap: (() -> Unit)? by onTapLD

    private val iconLD = MutableSKLiveData(iconInitial)
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
