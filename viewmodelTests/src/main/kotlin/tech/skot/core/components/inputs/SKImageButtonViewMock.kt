package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentViewMock
import tech.skot.core.view.Icon

class SKImageButtonViewMock(
    onTapInitial: (() -> Unit)?,
    iconInitial: Icon,
    enabledInitial: Boolean?,
    hiddenInitial: Boolean?
): SKComponentViewMock(), SKImageButtonVC {
    override var icon: Icon = iconInitial
    override var onTap: (() -> Unit)? = onTapInitial
    override var enabled: Boolean? = enabledInitial
    override var hidden: Boolean? = hiddenInitial

    fun userTap() {
        onTap?.invoke()
    }
}

fun SKImageButtonVC.userTap() {
    (this as SKImageButtonViewMock).userTap()
}