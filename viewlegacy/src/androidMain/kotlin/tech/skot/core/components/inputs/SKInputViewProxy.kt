package tech.skot.core.components.inputs

import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.view.live.MutableSKLiveData

class SKInputViewProxy(
    override val maxSize: Int?,
    override val onDone: Function1<String?, Unit>?,
    override val onFocusLost: Function0<Unit>?,
    override val onInputText: Function1<String?, Unit>,
    override val type: SKInputVC.Type?,
    enabledInitial: Boolean?,
    errorInitial: String?,
    hiddenInitial: Boolean?,
    hintInitial: String?,
    textInitial: String?
) : SKComponentViewProxy<TextInputLayout>(), SKInputVC {
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




    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TextInputLayout,
        collectingObservers: Boolean
    ): SKInputView = SKInputView(activity, fragment, binding).apply {
        collectObservers = collectingObservers
        onMaxSize(maxSize)
        onOnDone(onDone)
        onOnFocusLost(onFocusLost)
        onOnInputText(onInputText)
        onType(type)
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
    }
}

interface SKInputRAI {
    fun onMaxSize(maxSize: Int?)

    fun onOnDone(onDone: Function1<String?, Unit>?)

    fun onOnFocusLost(onFocusLost: Function0<Unit>?)

    fun onOnInputText(onInputText: Function1<String?, Unit>)

    fun onType(type: SKInputVC.Type?)

    fun onEnabled(enabled: Boolean?)

    fun onError(error: String?)

    fun onHidden(hidden: Boolean?)

    fun onHint(hint: String?)

    fun onText(text: String?)
}