package tech.skot.core.components.inputs

import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Color


class SKInputWithSuggestions<D : Any?>(
    hint: String? = null,
    errorInitial: String? = null,
    initialChoices: List<D> = emptyList(),
    enabled: Boolean = true,
    onSelected: ((data: D) -> Unit)? = null,
    label: ((data: D) -> String)? = null,
    inputText: ((data: D) -> String)? = null,
    textColor: ((data: D) -> Color)? = null,
    striked: ((data: D) -> Boolean)? = null,
    oldSchoolModeHint: Boolean = false,
    onInputText: (input: String?) -> Unit

) : SKBaseCombo<D, SKInputWithSuggestionsVC>(
    onSelected = onSelected,
    label = label,
    inputText = inputText,
    textColor = textColor,
    striked = striked
) {
    override val view = coreViewInjector.inputWithSuggestions(
        hint = hint,
        errorInitial = errorInitial,
        choicesInitial = initialChoices.map { it.toChoice() },
        onSelected = onSelectedLambda(),
        selectedInitial = null,
        enabledInitial = enabled,
        hiddenInitial = null,
        onInputText = onInputText,
        dropDownDisplayedInitial = false,
        oldSchoolModeHint = oldSchoolModeHint
    )


}