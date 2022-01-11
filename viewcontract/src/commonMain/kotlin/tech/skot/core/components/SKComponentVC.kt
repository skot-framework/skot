package tech.skot.core.components

import tech.skot.core.view.SKPermission
import tech.skot.core.view.Style

interface SKComponentVC {
    fun displayErrorMessage(message: String)
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
}