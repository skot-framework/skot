package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView
import tech.skot.core.view.Icon


@SKLayoutIsSimpleView
interface SKImageButtonVC: SKComponentVC {
    var icon:Icon
    var onTap:(()->Unit)?
    var enabled:Boolean?
    var hidden:Boolean?
}