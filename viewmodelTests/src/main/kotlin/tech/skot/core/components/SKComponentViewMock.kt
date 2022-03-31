package tech.skot.core.components

import tech.skot.core.view.SKPermission
import tech.skot.core.view.Style

abstract class SKComponentViewMock : SKComponentVC {
    override var style: Style? = null


    var closeKeyBoardCounter = 0
    override fun closeKeyboard() {
        closeKeyBoardCounter++
    }

    val displayMessageMessages: MutableList<SKComponentVC.Message> = mutableListOf()
    override fun displayMessage(message: SKComponentVC.Message) {
        displayMessageMessages.add(message)
    }

    @Deprecated("Use displayMessageMessages and filter on type SKComponentVC.Message.Error")
    val displayErrorMessages: List<String>
        get() = displayMessageMessages.filterIsInstance<SKComponentVC.Message.Error>().map { it.content }

    override fun displayErrorMessage(message: String) {
        displayMessageMessages.add(SKComponentVC.Message.Error(message))
    }

    var permissionsOk = emptyList<SKPermission>()
    override fun requestPermissions(
        permissions: List<SKPermission>,
        onResult: (permissionsOk: List<SKPermission>) -> Unit
    ) {
        onResult(permissions.filter { permissionsOk.contains(it) })
    }

    override fun hasPermission(vararg permission: SKPermission): Boolean {
        return permission.all {
            permissionsOk.contains(it)
        }
    }

    var removed = false
    override fun onRemove() {
        removed = true
    }
}