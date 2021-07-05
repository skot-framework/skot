package tech.skot.core.components.inputs


import android.widget.ImageButton
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.view.Icon
import tech.skot.view.extensions.setOnClick
import tech.skot.view.extensions.setVisible

class SKImageButtonView(
    override val proxy: SKImageButtonViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    button: ImageButton
) : SKComponentView<ImageButton>(proxy, activity, fragment, button) {

    fun onOnTap(onTap: (() -> Unit)?) {
        binding.setOnClick(onTap)
    }

    fun onIcon(icon: Icon) {
        binding.setImageResource(icon.res)
    }

    fun onEnabled(enabled: Boolean?) {
        enabled?.let {
            binding.isEnabled = it
        }

    }

    fun onHidden(hidden: Boolean?) {
        hidden?.let { binding.setVisible(!it) }
    }

}