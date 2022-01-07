package tech.skot.core.components

open class SKScreenViewMock: SKComponentViewMock(), SKScreenVC {
    override var onBackPressed: (() -> Unit)? = null

    fun backPressed() {
        onBackPressed?.invoke()
    }
}

fun SKScreenVC.backPressed() {
    (this as SKScreenViewMock).backPressed()
}