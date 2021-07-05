package tech.skot.core.components

interface SKComponentVC {
    fun displayErrorMessage(message:String)
    fun closeKeyboard()
    fun onRemove()
}