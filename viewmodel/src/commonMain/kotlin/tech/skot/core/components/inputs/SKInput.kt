package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

open class SKInput(
    hint: String? = null,
    protected val nullable: Boolean = true,
    onDone: ((text: String?) -> Unit)? = null,
    viewType: SKInputVC.Type? = null,
    protected val defaultErrorMessage: String? = null,
    private val maxSize: Int? = null,
    private val afterValidation: (() -> Unit)? = null
    ) : SKComponent<SKInputVC>() {

    sealed class Validity(val errorMessage: String?) {
        abstract val isValid: Boolean

        object Valid : Validity(null) {
            override val isValid = true
        }

        class Error(errorMessage: String?) : Validity(errorMessage) {
            override val isValid = false
        }
    }

    private var _value: String? = null
    var value: String?
        get() = _value
        set(newVal) {
            onNewValue(newVal)
        }

    protected open fun format(str: String?): String? {
        return str
    }

    protected open fun validate(str: String?): Validity {
        return if (nullable || (str != null && str.isNotBlank() && (maxSize == null || str.length <= maxSize))) {
            Validity.Valid
        } else {
            Validity.Error(defaultErrorMessage)
        }
    }

    val isValid: Boolean
        get() = validity.isValid


    private fun onNewValue(str: String?) {
        if (_value != str && !(str == "" && _value == null)) {
            val formated = format(str)
            view.text = formated
            _value = formated
            validate(formated).let { newValidity ->
                validity = newValidity
                afterValidation?.invoke()
                if (validity.isValid) {
                    view.error = null
                }
            }
        }
    }

    protected val _error = Validity.Error(defaultErrorMessage)


    private var validity: Validity = if (nullable) Validity.Valid else _error

    private fun onFocusLost() {
        view.error = validity.errorMessage
    }


    override val view = coreViewInjector.input(
        onInputText = {
            onNewValue(it)
        },
        type = viewType,
        maxSize = maxSize,
        onFocusLost =
        {
            onFocusLost()
        },
        onDone = onDone,
        hintInitial = hint,
        textInitial = null,
        errorInitial = null,
        hiddenInitial = false,
        enabledInitial = true
    )
}

open class SKInputRegExp(
    private val regex:Regex,
    hint: String? = null,
    nullable: Boolean = true,
    onDone: ((text: String?) -> Unit)? = null,
    viewType: SKInputVC.Type? = null,
    defaultErrorMessage: String? = null,
    maxSize: Int? = null,
    afterValidation: (() -> Unit)? = null
): SKInput(hint, nullable, onDone, viewType, defaultErrorMessage, maxSize, afterValidation) {
    override fun validate(str: String?): Validity {
        val superValidity = super.validate(str)
        return if (superValidity == Validity.Valid) {
            when  {
                !str.isNullOrBlank() && !regex.matches(str) -> Validity.Error(errorMessage = defaultErrorMessage)
                else -> Validity.Valid
            }
        }
        else {
            superValidity
        }
    }
}