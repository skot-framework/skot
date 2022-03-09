package tech.skot.core.components.inputs

import android.widget.Button
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.view.extensions.setOnClick
import tech.skot.view.extensions.setVisible

class SKButtonView(
    override val proxy: SKButtonViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    button: Button
) : SKComponentView<Button>(proxy, activity, fragment, button) {

    fun onOnTap(onTap: (() -> Unit)?) {
        binding.setOnClick(onTap, single = proxy.debounce != null, delay = proxy.debounce ?: 500)
    }

    fun onLabel(label: String?) {
        label?.let { binding.text = it }
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