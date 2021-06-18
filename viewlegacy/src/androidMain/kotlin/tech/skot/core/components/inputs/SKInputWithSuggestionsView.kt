package tech.skot.core.components.inputs

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.viewlegacy.databinding.SkComboBinding
import tech.skot.viewlegacy.databinding.SkInputWithSuggestionsBinding

class SKInputWithSuggestionsView(
    activity: SKActivity,
    fragment: Fragment?,
    binding: SkInputWithSuggestionsBinding
) : SKCommonComboView<SkInputWithSuggestionsBinding>(activity, fragment, binding,  binding.root, binding.autoComplete) {

    private var firstChangeDoneFor = false

    private var watching:TextWatcher? = null

    private var typedText:String? = null
    init {
        autoComplete.setOnFocusChangeListener { v, hasFocus ->
            SKLog.d("SKInputWithSuggestionsView setOnFocusChangeListener $hasFocus")
        }
    }
    fun onOnInputText(onInputText: (String?) -> Unit) {
        val watcher = object : TextWatcher {
            // Un premier changement peut être lancé au premier affichage (pour passer de null à "" ??)
            // il ne faut pas en tenir compte, ce n'est pas une action utilisateur et cela peut écraser une value settée en amont
            override fun afterTextChanged(p0: Editable?) {
                val newText = p0?.toString()
                SKLog.d("onOnInputText ---- afterTextChanged   ${p0.toString()} firstChangeDoneFor=$firstChangeDoneFor newText=$newText lockSelectedReaction=$lockSelectedReaction")
                if (firstChangeDoneFor || !newText.isNullOrBlank()) {
                    if (!lockSelectedReaction && !(_choices.any { it.text == newText })) {
                        SKLog.d("onOnInputText ---- afterTextChanged will launch onInputText")
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

    override fun removeObservers() {
        super.removeObservers()
        watching?.let { autoComplete.removeTextChangedListener(it) }
    }


    fun requestFocus() {
        autoComplete.post {
            autoComplete.requestFocus()
        }
    }

    override fun onSelect(selected: SKComboVC.Choice?) {
        if (selected != null) {
            super.onSelect(selected)
        }
    }

}