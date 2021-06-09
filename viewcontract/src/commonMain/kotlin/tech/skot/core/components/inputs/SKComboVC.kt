package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentVC

interface SKComboVC: SKComponentVC {
    val hint: String?
    val onSelected: (newText: String) -> Unit
    var choices: List<Choice>
    var selected: Choice?
    var enabled: Boolean?

    data class Choice(
        val text: String,
        val strikethrough: Boolean = false,
        val colored: Boolean = false
    )
}