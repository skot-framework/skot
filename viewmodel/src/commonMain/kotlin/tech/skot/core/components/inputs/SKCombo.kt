package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

abstract class SKBaseCombo<D : Any?, V : SKComboVC>(
    private val label: ((data: D) -> String)?,
    private val inputText: ((data: D) -> String)?,
    private val colored: ((data: D) -> Boolean)?,
    private val striked: ((data: D) -> Boolean)?,
    private val onSelected: ((data: D) -> Unit)?,
    ) : SKComponent<V>() {

    var choices: List<D> = emptyList()
        set(newVal) {
            field = newVal
            view.choices = newVal.map { it.toChoice() }
        }

    protected fun D.toChoice(): SKComboVC.Choice {
        val label = label?.invoke(this) ?: toString()
        return SKComboVC.Choice(
            data = this,
            text = label,
            strikethrough = striked?.invoke(this) ?: false,
            colored = colored?.invoke(this) ?: false,
            inputText = inputText?.invoke(this) ?: label
        )
    }

    private var _value: D? = null
    var value: D?
        get() = _value
        set(newVal) {
            _value = newVal
            view.dropDownDisplayed = false
            view.selected = newVal?.toChoice()
        }

    fun onSelectedLambda(): (data: Any?) -> Unit =
        {
            (it as D).let {
                value = it
                onSelected?.invoke(it)
            }

        }
}


class SKCombo<D : Any?>(
    hint: String? = null,
    initialChoices: List<D> = emptyList(),
    enabled: Boolean = true,
    label: ((data: D) -> String)? = null,
    inputText: ((data: D) -> String)? = null,
    colored: ((data: D) -> Boolean)? = null,
    striked: ((data: D) -> Boolean)? = null,
    onSelected: ((data: D) -> Unit)? = null
    ) : SKBaseCombo<D, SKComboVC>(
    onSelected = onSelected,
    label = label,
    inputText = inputText,
    colored = colored,
    striked = striked
) {
    override val view = coreViewInjector.combo(
        hint = hint,
        choicesInitial = initialChoices.map { it.toChoice() },
        onSelected = onSelectedLambda(),
        selectedInitial = null,
        enabledInitial = enabled,
        hiddenInitial = null,
        dropDownDisplayedInitial = false
    )
}