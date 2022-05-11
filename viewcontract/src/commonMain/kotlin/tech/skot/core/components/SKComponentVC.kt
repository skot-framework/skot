package tech.skot.core.components

import tech.skot.core.components.inputs.SKInputVC
import tech.skot.core.components.presented.SKAlertVC
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

    sealed class Message() {
        abstract val content:String
        data class Debug(override val content:String): Message()
        data class Info(override val content:String):Message()
        data class Warning(override val content:String):Message()
        data class Error(override val content:String):Message()
        data class Alert(
            val title:String? = null,
            val message:String? = null,
            val cancelable:Boolean = false,
            val withInput:Boolean = false,
            val mainButton: SKAlertVC.Button,
            val secondaryButton: SKAlertVC.Button? = null
        ): Message() {
            override val content: String = title ?: message ?: ""
        }
    }
}