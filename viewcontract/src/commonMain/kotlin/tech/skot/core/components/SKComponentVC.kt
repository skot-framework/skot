package tech.skot.core.components

import tech.skot.core.view.Style

interface SKComponentVC {
    fun displayErrorMessage(message:String)
    fun closeKeyboard()
    fun onRemove()

    /**
     * Style qui ne sera appliqué qu'en theme pour les items de SKBox
     */
    var style: Style?
}