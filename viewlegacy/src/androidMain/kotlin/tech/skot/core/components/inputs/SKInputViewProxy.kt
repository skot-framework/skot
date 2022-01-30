package tech.skot.core.components.inputs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R

abstract class SKInputViewProxyCommon<V : View>(
    override val maxSize: Int?,
    override val onDone: Function1<String?, Unit>?,
    override val onFocusChange: Function1<Boolean, Unit>?,
    override val onInputText: Function1<String?, Unit>,
    override val type: SKInputVC.Type?,
    enabledInitial: Boolean?,
    errorInitial: String?,
    hiddenInitial: Boolean?,
    hintInitial: String?,
    textInitial: String?,
    showPasswordInitial: Boolean?
) : SKComponentViewProxy<V>(), SKInputVC {
    private val enabledLD: MutableSKLiveData<Boolean?> = MutableSKLiveData(enabledInitial)

    override var enabled: Boolean? by enabledLD

    private val errorLD: MutableSKLiveData<String?> = MutableSKLiveData(errorInitial)

    override var error: String? by errorLD

    private val hiddenLD: MutableSKLiveData<Boolean?> = MutableSKLiveData(hiddenInitial)

    override var hidden: Boolean? by hiddenLD

    private val hintLD: MutableSKLiveData<String?> = MutableSKLiveData(hintInitial)

    override var hint: String? by hintLD

    private val textLD: MutableSKLiveData<String?> = MutableSKLiveData(textInitial)

    override var text: String? by textLD

    private var showPasswordLD: MutableSKLiveData<Boolean?> = MutableSKLiveData(showPasswordInitial)

    override var showPassword: Boolean? by showPasswordLD

    private val requestFocusMessage = SKMessage<Unit>()
    override fun requestFocus() {
        requestFocusMessage.post(Unit)
    }




    abstract fun createView(
        activity: SKActivity,
        fragment: Fragment?,
        binding: V
    ): SKInputViewCommon<V>

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: V
    ): SKInputViewCommon<V> = createView(activity, fragment, binding).apply {
        onMaxSize(maxSize)
        onOnDone(onDone)
        onType(type)
        onOnFocusChange(onFocusChange)
        onOnInputText(onInputText)
        enabledLD.observe {
            onEnabled(it)
        }
        errorLD.observe {
            onError(it)
        }
        hiddenLD.observe {
            onHidden(it)
        }
        hintLD.observe {
            onHint(it)
        }
        textLD.observe {
            onText(it)
        }
        showPasswordLD.observe {
            onShowPassword(it)
        }
        requestFocusMessage.observe {
            requestFocus()
        }
    }

}


class SKInputViewProxy(
    maxSize: Int? = null,
    onDone: Function1<String?, Unit>? = null,
    onFocusChange: ((hasFocus:Boolean) -> Unit)? = null,
    onInputText: Function1<String?, Unit>,
    type: SKInputVC.Type? = null,
    enabledInitial: Boolean? = null,
    errorInitial: String? = null,
    hiddenInitial: Boolean? = null,
    hintInitial: String? = null,
    textInitial: String? = null,
    showPasswordInitial: Boolean? = null
) : SKInputViewProxyCommon<TextInputLayout>(
    maxSize,
    onDone,
    onFocusChange,
    onInputText,
    type,
    enabledInitial,
    errorInitial,
    hiddenInitial,
    hintInitial,
    textInitial,
    showPasswordInitial,
) {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_input

    override fun createView(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TextInputLayout
    ) = SKInputView(this, activity, fragment, binding)
}


class SKSimpleInputViewProxy(
    maxSize: Int? = null,
    onDone: Function1<String?, Unit>? = null,
    onFocusChange: ((hasFocus:Boolean) -> Unit)? = null,
    onInputText: Function1<String?, Unit>,
    type: SKInputVC.Type? = null,
    enabledInitial: Boolean? = null,
    errorInitial: String? = null,
    hiddenInitial: Boolean? = null,
    hintInitial: String? = null,
    textInitial: String? = null,
    showPasswordInitial: Boolean? = null
) : SKInputViewProxyCommon<EditText>(
    maxSize,
    onDone,
    onFocusChange,
    onInputText,
    type,
    enabledInitial,
    errorInitial,
    hiddenInitial,
    hintInitial,
    textInitial,
    showPasswordInitial
), SKSimpleInputVC {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_simple_input

    override fun createView(
        activity: SKActivity,
        fragment: Fragment?,
        binding: EditText
    ) = SKSimpleInputView(this, activity, fragment, binding)


}


interface SKInputRAI {
    fun onMaxSize(maxSize: Int?)

    fun onOnDone(onDone: Function1<String?, Unit>?)

    fun onOnFocusChange(onFocusChange: Function1<Boolean, Unit>?)

    fun onOnInputText(onInputText: Function1<String?, Unit>)

    fun onType(type: SKInputVC.Type?)

    fun onEnabled(enabled: Boolean?)

    fun onError(error: String?)

    fun onHidden(hidden: Boolean?)

    fun onHint(hint: String?)

    fun onText(text: String?)

    fun onShowPassword(showPassword: Boolean?)
}