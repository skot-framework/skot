package tech.skot.components

import tech.skot.contract.Private

interface ScreenView : ComponentView {
    @Private
    var loading: Boolean

    @Private
    var onBack: (() -> Unit)?

    @Private
    var onTop: ScreenView?

    @Private
    val key: Long

    fun openScreenWillFinish(screenToOpen:ScreenView)
    fun showBottomSheetDialog(screen: ScreenView)
}