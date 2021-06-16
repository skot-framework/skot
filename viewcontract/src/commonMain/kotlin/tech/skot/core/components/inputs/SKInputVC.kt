package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView

@SKLayoutIsSimpleView
interface SKInputVC: SKComponentVC {

    sealed class Type {
        object Normal : Type()
        object Number : Type()
        object Phone : Type()
        object NumberPassword : Type()
        object LongText : Type()
        object EMail: Type()
    }

    val onInputText: (newText: String?) -> Unit

    val type: Type?
    val maxSize: Int?
    val onFocusLost: (() -> Unit)?
    val onDone: ((text: String?) -> Unit)?

    var hint: String?
    var text: String?
    var error: String?
    var hidden: Boolean?
    var enabled: Boolean?

    fun requestFocus()

}