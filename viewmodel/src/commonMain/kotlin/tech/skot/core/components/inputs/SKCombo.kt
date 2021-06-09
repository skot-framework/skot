package tech.skot.core.components.inputs

import tech.skot.core.SKLog
import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

open class ComboChoiceMapper<D : Any> {
    open fun label(d: D): String = d.toString()
    open fun strikethrough(d: D) = false
    open fun colored(d: D) = false

    fun comboViewChoice(d: D) = let { SKComboVC.Choice(label(d), strikethrough(d), colored(d)) }
    fun comboViewChoices(data: List<D>): List<SKComboVC.Choice> = data.map { comboViewChoice(it) }
}

open class SKCombo<D:Any>(
    var choiceMapper: ComboChoiceMapper<D> = ComboChoiceMapper(),
    hint: String? = null,
    initialChoices: List<D> = emptyList(),
    enabled:Boolean = true,
    private val nullable: Boolean = false,
    private val nullManager: NullManager? = null,
    private val onSelected: ((choice: D) -> Unit)? = null
): SKComponent<SKComboVC>() {

    private val nullChoice = nullManager?.let { SKComboVC.Choice(it.label) }
    data class NullManager(val label: String, val onNull: () -> Unit)

    var choices: List<D> = initialChoices
        set(newVal) {
            field = newVal
            view.choices = listOfNotNull(nullChoice) + choiceMapper.comboViewChoices(newVal)
        }

    private var _value: D? = null
    var value: D?
        get() = _value
        set(newVal) {
            _value = newVal
            SKLog.d("§§§§§§§§§§§ set new val $newVal")
            view.selected = newVal?.let { choiceMapper.comboViewChoice(it) }
            SKLog.d("§§§§§§§§§§§ view.selected ${view.selected}")
            isValid = nullable || newVal != null
        }

    var isValid: Boolean = nullable

    override val view = coreViewInjector.combo(
        hint = hint,
        choicesInitial = listOfNotNull(nullChoice) + choiceMapper.comboViewChoices(initialChoices),
        onSelected = {
            selectLabel(it)
        },
        selectedInitial = null,
        enabledInitial = enabled
    )

    private fun selectLabel(label: String) {
        if (nullManager != null && label == nullManager.label) {
            nullManager.onNull.invoke()
        } else {
            choices.find { choiceMapper.label(it) == label }?.let { choice ->
                isValid = true
                _value = choice
                onSelected?.invoke(choice)
            }
        }
    }
}