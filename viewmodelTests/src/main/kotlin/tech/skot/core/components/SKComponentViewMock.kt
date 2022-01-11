package tech.skot.core.components

import tech.skot.core.view.SKPermission
import tech.skot.core.view.Style

abstract class SKComponentViewMock : SKComponentVC {
    override var style: Style? = null


    var closeKeyBoardCounter = 0
    override fun closeKeyboard() {
        closeKeyBoardCounter++
    }

    val displayErrorMessages: MutableList<String> = mutableListOf()
    override fun displayErrorMessage(message: String) {
        displayErrorMessages.add(message)
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