package tech.skot.core.components.inputs

import tech.skot.core.di.coreViewInjector


class SKInputWithSuggestions<D : Any?>(
    hint: String? = null,
    errorInitial: String? = null,
    initialChoices: List<D> = emptyList(),
    enabled: Boolean = true,
    onSelected: ((data: D) -> Unit)? = null,
    label: ((data: D) -> String)? = null,
    inputText: ((data: D) -> String)? = null,
    colored: ((data: D) -> Boolean)? = null,
    striked: ((data: D) -> Boolean)? = null,
    onInputText: (input: String?) -> Unit

) : SKBaseCombo<D, SKInputWithSuggestionsVC>(
    onSelected = onSelected,
    label = label,
    inputText = inputText,
    colored = colored,
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
        dropDownDisplayedInitial = false
    )


}