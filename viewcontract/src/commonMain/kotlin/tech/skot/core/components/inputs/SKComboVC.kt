package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentVC

interface SKComboVC: SKComponentVC {
    val hint: String?
    val onSelected: ((choice: Any?) -> Unit)?
    var choices: List<Choice>
    var selected: Choice?
    var enabled: Boolean?
    var hidden:Boolean?
    var dropDownDisplayed:Boolean

    class Choice(
        val data:Any?,
        val text: String = data.toString(),
        val strikethrough: Boolean = false,
        val colored: Boolean = false,
        val inputText:String = text,
    )
}