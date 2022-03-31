package tech.skot.core.components

import tech.skot.core.components.inputs.SKInputVC
import tech.skot.core.view.SKPermission
import tech.skot.core.view.Style

interface SKComponentVC {
    fun displayMessage(message:Message)

    @Deprecated("Use  SKComponent.displayMessageError(message) or  view.displayMessage(SKComponentVC.Message.Error(message))")
    fun displayErrorMessage(message:String)

    fun closeKeyboard()
    fun onRemove()

    fun requestPermissions(
        permissions: List<SKPermission>,
        onResult: (permissionsOk: List<SKPermission>) -> Unit
    )

    fun hasPermission(vararg permission: SKPermission): Boolean

    /**
     * Style qui ne sera appliqué qu'en theme pour les items de SKBox
     */
    var style: Style?

    sealed class Message(val content:String) {
        class Debug(content:String): Message(content)
        class Info(content:String):Message(content)
        class Warning(content:String):Message(content)
        class Error(content:String):Message(content)
    }
}