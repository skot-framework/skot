package tech.skot.core.components.inputs

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding
import tech.skot.viewlegacy.databinding.SkInputWithSuggestionsBinding


class SKInputWithSuggestionsViewProxy(
    hint: String?,
    onSelected: ((choice: Any?) -> Unit)?,
    choicesInitial: List<SKComboVC.Choice>,
    selectedInitial: SKComboVC.Choice?,
    enabledInitial: Boolean?,
    hiddenInitial: Boolean?,
    dropDownDisplayedInitial: Boolean,
    override val onInputText: (input: String?) -> Unit,
) : SKCommonComboViewProxy<SkInputWithSuggestionsBinding>(
    hint,
    onSelected,
    choicesInitial,
    selectedInitial,
    enabledInitial,
    hiddenInitial,
    dropDownDisplayedInitial
),
    SKInputWithSuggestionsVC {

    override val layoutId: Int = R.layout.sk_input_with_suggestions
    override fun bindingOf(view: View) = SkInputWithSuggestionsBinding.bind(view)

    private val requestFocusMessage = SKMessage<Unit>()
    override fun requestFocus() {
        requestFocusMessage.post(Unit)
    }

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: SkInputWithSuggestionsBinding
    ) = SKInputWithSuggestionsView(this, activity, fragment, binding).apply {
        bind()
        onOnInputText(onInputText)
        requestFocusMessage.observe {
            requestFocus()
        }

    }
}