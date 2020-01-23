package tech.skot.contract.components

interface ScreenView : ComponentView {
    var loading: Boolean
    var onBack: (() -> Unit)?

    var onTop: ScreenView?

    val key: Long
}