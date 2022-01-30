package tech.skot.core.components.inputs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding
import tech.skot.viewlegacy.databinding.SkInputWithSuggestionsBinding


class SKComboViewProxy(
    hint: String?,
    errorInitial: String?,
    onSelected: ((choice: Any?) -> Unit)?,
    choicesInitial: List<SKComboVC.Choice>,
    selectedInitial: SKComboVC.Choice?,
    enabledInitial: Boolean?,
    hiddenInitial: Boolean?,
    dropDownDisplayedInitial: Boolean
) : SKCommonComboViewProxy<SkComboBinding>(
    hint = hint,
    errorInitial = errorInitial,
    onSelected = onSelected,
    choicesInitial = choicesInitial,
    selectedInitial = selectedInitial,
    enabledInitial = enabledInitial,
    hiddenInitial = hiddenInitial,
    dropDownDisplayedInitial = dropDownDisplayedInitial
) {
    override val layoutId: Int = R.layout.sk_combo
    override fun bindingOf(view: View) = SkComboBinding.bind(view)

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: SkComboBinding
    ) = SKComboView(this, activity, fragment, binding).apply {
        bind()
    }

    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): SkComboBinding  = SkComboBinding.inflate(layoutInflater, parent, attachToParent).also {
        it.root.tag = this.hashCode()
    }
}

abstract class SKCommonComboViewProxy<Binding : Any>(
    override val hint: String?,
    errorInitial: String?,
    override val onSelected: ((choice: Any?) -> Unit)?,
    choicesInitial: List<SKComboVC.Choice>,
    selectedInitial: SKComboVC.Choice?,
    enabledInitial: Boolean?,
    hiddenInitial: Boolean?,
    dropDownDisplayedInitial: Boolean,
) : SKComponentViewProxy<Binding>(), SKComboVC {


    private val choicesLD = MutableSKLiveData(choicesInitial)
    override var choices by choicesLD

    private val selectedLD = MutableSKLiveData(selectedInitial)
    override var selected: SKComboVC.Choice? by selectedLD

    private val enabledLD = MutableSKLiveData(enabledInitial)
    override var enabled by enabledLD

    private val hiddenLD = MutableSKLiveData(hiddenInitial)
    override var hidden by hiddenLD

    private val errorLD = MutableSKLiveData(errorInitial)
    override var error by errorLD


    private val dropDownDisplayedLD = MutableSKLiveData(dropDownDisplayedInitial)
    override var dropDownDisplayed: Boolean by dropDownDisplayedLD


    fun SKCommonComboView<Binding>.bind() {
        onHint(hint)
        onOnSelected(onSelected)
        errorLD.observe {
            onError(it)
        }
        choicesLD.observe {
            onChoices(it)
        }
        selectedLD.observe {
            onSelect(it)
        }
        enabledLD.observe {
            onEnabled(it)
        }
        hiddenLD.observe {
            onHidden(it)
        }
        dropDownDisplayedLD.observe {
            onDropDownDisplayed(it)
        }
    }

}
