package tech.skot.core.components.inputs

import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.view.extensions.setOnDone
import tech.skot.view.extensions.setVisible

abstract class SKInputViewCommon<V : View>(
    override val proxy: SKInputViewProxyCommon<V>,
    activity: SKActivity,
    fragment: Fragment?,
    view: V,
) : SKComponentView<V>(proxy, activity, fragment, view), SKInputRAI {

    abstract val editText: EditText

    override fun onMaxSize(maxSize: Int?) {
        maxSize?.let { editText.filters += InputFilter.LengthFilter(maxSize) }
    }

    override fun onOnDone(onDone: ((String?) -> Unit)?) {
        editText.setOnDone(onDone)
    }

    override fun onOnFocusChange(onFocusChange: ((Boolean) -> Unit)?) {
        if (onFocusChange != null) {
            editText.setOnFocusChangeListener { v, hasFocus ->
                onFocusChange.invoke(hasFocus)
            }
        }
    }


    private var firstChangeDoneFor = false

    private var watching: TextWatcher? = null

    override fun onOnInputText(onInputText: (String?) -> Unit) {
        val watcher = object : TextWatcher {
            // Un premier changement peut être lancé au premier affichage (pour passer de null à "" ??)
            // il ne faut pas en tenir compte, ce n'est pas une action utilisateur et cela peut écraser une value settée en amont
            override fun afterTextChanged(p0: Editable?) {
                val newText = p0?.toString()
                if (firstChangeDoneFor || !newText.isNullOrBlank()) {
                    onInputText(p0?.toString())
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
        watching?.let { editText.removeTextChangedListener(it) }
        editText.addTextChangedListener(watcher)
        watching = watcher
    }

    override fun onRecycle() {
        super.onRecycle()
        watching?.let { editText.removeTextChangedListener(it) }
    }

    override fun onType(type: SKInputVC.Type?) {
        if (type != null) {
            editText.inputType = when (type) {
                SKInputVC.Type.EMail -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                SKInputVC.Type.Phone -> InputType.TYPE_CLASS_PHONE
                SKInputVC.Type.Password -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                SKInputVC.Type.PasswordWithDefaultHintFont -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                SKInputVC.Type.Number -> InputType.TYPE_CLASS_NUMBER
                SKInputVC.Type.NumberPassword -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                SKInputVC.Type.LongText -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                SKInputVC.Type.TextCapSentences -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                SKInputVC.Type.AllCaps -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                SKInputVC.Type.Normal -> InputType.TYPE_CLASS_TEXT
            }
            if (type == SKInputVC.Type.PasswordWithDefaultHintFont) {
                editText.typeface = Typeface.DEFAULT
            }
        }
    }

    override fun onShowPassword(showPassword: Boolean?) {
        showPassword?.let {
            val selection = editText.selectionStart
            editText.transformationMethod =
                if (it) null else PasswordTransformationMethod.getInstance()
            editText.setSelection(selection)
        }

    }


    override fun onText(text: String?) {
        val oldValue = editText.text.toString()
        if (text.isNullOrBlank()) {
            if (oldValue.isNotBlank()) {
                editText.setText(text)
            }
        } else if (text != oldValue) {
            editText.text.replace(0, oldValue.length, text)
        }
    }

    fun requestFocus() {
        editText.post {
            editText.requestFocus()
        }
    }

}

class SKInputView(
    override val proxy: SKInputViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val textInputLayout: TextInputLayout,
) : SKInputViewCommon<TextInputLayout>(proxy, activity, fragment, textInputLayout) {

    override val editText: EditText = textInputLayout.editText!!

    override fun onEnabled(enabled: Boolean?) {
        enabled?.let { textInputLayout.isEnabled = it }
    }

    override fun onError(error: String?) {
        textInputLayout.error = error
    }

    override fun onHidden(hidden: Boolean?) {
        hidden?.let { binding.setVisible(!it) }
    }

    override fun onHint(hint: String?) {
        textInputLayout.hint = hint
    }
}

class SKSimpleInputView(
    override val proxy: SKSimpleInputViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    override val editText: EditText,
) : SKInputViewCommon<EditText>(proxy, activity, fragment, editText) {


    override fun onEnabled(enabled: Boolean?) {
        enabled?.let { editText.isEnabled = it }
    }

    override fun onError(error: String?) {
        editText.error = error
    }

    override fun onHidden(hidden: Boolean?) {
        hidden?.let { binding.setVisible(!it) }
    }

    override fun onHint(hint: String?) {
        editText.hint = hint
    }
}