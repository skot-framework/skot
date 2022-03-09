package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Icon

open class SKImageButton(
    icon: Icon,
    enabled:Boolean? = null,
    hidden:Boolean? = null,
    debounce:Long? = 500,
    onTap:(()->Unit)? = null,
    ): SKComponent<SKImageButtonVC>() {

    override val view = coreViewInjector.imageButton(
        onTapInitial = onTap,
        iconInitial = icon,
        enabledInitial = enabled,
        hiddenInitial = hidden,
        debounce = debounce
    )


}