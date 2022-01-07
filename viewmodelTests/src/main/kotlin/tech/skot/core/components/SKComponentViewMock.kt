package tech.skot.core.components

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

    var removed = false
    override fun onRemove() {
        removed = true
    }
}