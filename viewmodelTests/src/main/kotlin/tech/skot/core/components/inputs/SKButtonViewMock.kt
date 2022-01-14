package tech.skot.core.components.inputs

import tech.skot.core.SKLog
import tech.skot.core.components.SKComponentViewMock
import tech.skot.core.view.Style

open class SKButtonViewMock(
    onTapInitial: (() -> Unit)? = null,
    labelInitial: String? = null,
    enabledInitial: Boolean? = null,
    hiddenInitial: Boolean? = null
) : SKComponentViewMock(), SKButtonVC {
    override var enabled: Boolean? = enabledInitial
    override var hidden: Boolean? = hiddenInitial
    override var label: String? = labelInitial
    override var onTap: (() -> Unit)? = onTapInitial
    override var style: Style? = null

    fun userTap() {
        if (enabled != false) {
            onTap?.invoke()
        }
    }

}


fun SKButtonVC.userTap() {
    (this as SKButtonViewMock).userTap()
}