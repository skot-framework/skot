package tech.skot.components

interface ScreenView : ComponentView {
    var loading: Boolean
    var onBack: (() -> Unit)?

    var onTop: ScreenView?

    val key: Long

    fun openScreenWillFinish(screenToOpen:ScreenView)
}