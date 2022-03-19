package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Color

abstract class SKBaseCombo<D : Any?, V : SKComboVC>(
    private val label: ((data: D) -> String)?,
    private val inputText: ((data: D) -> String)?,
    private val textColor: ((data: D) -> Color)?,
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
            textColor = textColor?.invoke(this),
            inputText = inputText?.invoke(this) ?: label
        )
    }

    private var _value: D? = null
    var value: D?
        get() = _value
        set(newVal) {
            _value = newVal
            view.dropDownDisplayed = false
            view.selected = newVal?.toChoice() ?: view.choices.find { it.data == null }
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
    error: String? = null,
    initialChoices: List<D> = emptyList(),
    enabled: Boolean = true,
    label: ((data: D) -> String)? = null,
    inputText: ((data: D) -> String)? = null,
    textColor: ((data: D) -> Color)? = null,
    striked: ((data: D) -> Boolean)? = null,
    oldSchoolModeHint: Boolean = false,
    onSelected: ((data: D) -> Unit)? = null
) : SKBaseCombo<D, SKComboVC>(
    onSelected = onSelected,
    label = label,
    inputText = inputText,
    textColor = textColor,
    striked = striked
) {
    override val view = coreViewInjector.combo(
        hint = hint,
        errorInitial = error,
        choicesInitial = initialChoices.map { it.toChoice() },
        onSelected = onSelectedLambda(),
        selectedInitial = null,
        enabledInitial = enabled,
        hiddenInitial = null,
        dropDownDisplayedInitial = false,
        oldSchoolModeHint = oldSchoolModeHint
    )
}