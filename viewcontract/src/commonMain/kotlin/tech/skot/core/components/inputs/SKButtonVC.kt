package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView

@SKLayoutIsSimpleView
interface SKButtonVC: SKComponentVC {
    var onTap:(()->Unit)?
    var label:String?
    var enabled:Boolean?
    var hidden:Boolean?
    /**
     * null signifie pas de debounce
     */
    val debounce:Long?
}