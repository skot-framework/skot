package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Icon

/**
 * SKImageButton is as [SKComponent] that could be used to show an ImageButton
 * @param icon the initial [Icon] of the ImageButton
 * @param enabled the initial [enable][Boolean] state of the imageButton
 * @param hidden the initial [visibility][Boolean] state of the imageButton
 * @param debounce the [period][Long] during which second tap is not taken into account
 * @param onTap the initial on tap callback lambda
 */
@Suppress("unused")
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

    /**
     *  use it to set/get the [enable][Boolean] state of the imageButton
     */
    var enabled: Boolean?
        get() = view.enabled
        set(value) {
            view.enabled = value
        }

    /**
     *  use it to set/get the [visibility][Boolean] state of the imageButton
     */
    var hidden: Boolean?
        get() = view.hidden
        set(value) {
            view.hidden = value
        }

    /**
     *  use it to set/get the [icon][Icon] of the imageButton
     */
    var icon: Icon
        get() = view.icon
        set(value) {
            view.icon = value
        }

    /**
     *  use it to set/get the onTap callback lambda of the imageButton
     */
    var onTap: (() -> Unit)?
        get() =  view.onTap
    set(value) {
        view.onTap = value
    }

}