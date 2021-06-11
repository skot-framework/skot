package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

open class SKButton(
    label:String? = null,
    enabled:Boolean? = null,
    hidden:Boolean? = null,
    onTap:(()->Unit)? = null,
    ): SKComponent<SKButtonVC>() {

    override val view = coreViewInjector.button(
        onTapInitial = onTap,
        labelInitial = label,
        enabledInitial = enabled,
        hiddenInitial = hidden,

    )

}