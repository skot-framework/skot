package tech.skot.core.components.inputs

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding

class SKComboViewProxy(
    override val hint: String?,
    override val onSelected: (newText: String) -> Unit,
    choicesInitial: List<SKComboVC.Choice>,
    selectedInitial: SKComboVC.Choice?,
    enabledInitial:Boolean?
) : SKComponentViewProxy<SkComboBinding>(), SKComboVC {

    override val layoutId: Int? = R.layout.sk_combo

    private val choicesLD = MutableSKLiveData(choicesInitial)
    override var choices by choicesLD

    private val selectedLD = MutableSKLiveData(selectedInitial)
    override var selected: SKComboVC.Choice? by selectedLD

    private val enabledLD = MutableSKLiveData(enabledInitial)
    override var enabled by enabledLD

    override fun bindingOf(view: View) = SkComboBinding.bind(view)

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: SkComboBinding,
        collectingObservers: Boolean
    ) = SKComboView(activity, fragment, binding).apply {
        collectObservers = collectObservers
        onHint(hint)
        onOnSelected(onSelected)
        choicesLD.observe {
            onChoices(it)
        }
        selectedLD.observe {
            onSelect(it)
        }
        enabledLD.observe {
            onEnabled(it)
        }
    }
}