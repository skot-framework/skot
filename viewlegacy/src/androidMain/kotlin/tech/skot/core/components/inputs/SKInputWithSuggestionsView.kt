package tech.skot.core.components.inputs

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkInputWithSuggestionsBinding

class SKInputWithSuggestionsView(
    override val proxy: SKInputWithSuggestionsViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: SkInputWithSuggestionsBinding
) : SKCommonComboView<SkInputWithSuggestionsBinding>(
    proxy,
    activity,
    fragment,
    binding,
    binding.root,
    binding.autoComplete,
    R.layout.sk_input_choice_item
) {

    private var firstChangeDoneFor = false

    private var watching: TextWatcher? = null


    fun onOnInputText(onInputText: (String?) -> Unit) {
        val watcher = object : TextWatcher {
            // Un premier changement peut être lancé au premier affichage (pour passer de null à "" ??)
            // il ne faut pas en tenir compte, ce n'est pas une action utilisateur et cela peut écraser une value settée en amont
            override fun afterTextChanged(p0: Editable?) {
                val newText = p0?.toString()
                if (firstChangeDoneFor || !newText.isNullOrBlank()) {
                    if (!lockSelectedReaction && !(_choices.any { it.text == newText })) {
                        onInputText(newText)
                    }
                }
                firstChangeDoneFor = true
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nu
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nu
            }
        }
        watching?.let { autoComplete.removeTextChangedListener(it) }
        autoComplete.addTextChangedListener(watcher)
        watching = watcher
    }

    override fun onRecycle() {
        super.onRecycle()
        watching?.let { autoComplete.removeTextChangedListener(it) }
    }


    fun requestFocus() {
        autoComplete.post {
            autoComplete.requestFocus()
        }
    }

}