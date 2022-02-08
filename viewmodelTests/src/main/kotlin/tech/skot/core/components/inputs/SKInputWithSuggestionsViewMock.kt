package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentViewMock

class SKInputWithSuggestionsViewMock(
    hint: String?,
    errorInitial: String?,
    onSelected: ((choice: Any?) -> Unit)?,
    choicesInitial: List<SKComboVC.Choice>,
    selectedInitial: SKComboVC.Choice?,
    enabledInitial: Boolean?,
    hiddenInitial: Boolean?,
    dropDownDisplayedInitial: Boolean,
    onInputText: (input: String?) -> Unit,
    override val oldSchoolModeHint: Boolean
) : SKComponentViewMock(), SKInputWithSuggestionsVC {
    override val onInputText: (input: String?) -> Unit = onInputText
    override var error: String? = errorInitial
    override val hint: String? = hint
    override val onSelected: ((choice: Any?) -> Unit)? = onSelected
    override var choices: List<SKComboVC.Choice> = choicesInitial
    override var selected: SKComboVC.Choice? = selectedInitial
    override var enabled: Boolean? = enabledInitial
    override var hidden: Boolean? = hiddenInitial
    override var dropDownDisplayed: Boolean = dropDownDisplayedInitial

    var requestFocusCounter = 0
    override fun requestFocus() {
        requestFocusCounter++
    }
}

fun SKInputWithSuggestionsVC.requestFocusCount(): Int =
    (this as SKInputViewMock).requestFocusCounter