package tech.skot.core.components.inputs

interface SKInputWithSuggestionsVC : SKComboVC {
    val onInputText:(input:String?) -> Unit
    var text: String?
    fun requestFocus()
}