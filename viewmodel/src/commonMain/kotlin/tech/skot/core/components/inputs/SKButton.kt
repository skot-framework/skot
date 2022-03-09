package tech.skot.core.components.inputs

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

open class SKButton(
    label: String? = null,
    enabled: Boolean? = null,
    hidden: Boolean? = null,
    debounce:Long? = 500,
    onTap: (() -> Unit)? = null,
) : SKComponent<SKButtonVC>() {

    override val view = coreViewInjector.button(
        onTapInitial = onTap,
        labelInitial = label,
        enabledInitial = enabled,
        hiddenInitial = hidden,
        debounce = debounce
    )

    var enabled: Boolean?
        get() = view.enabled
        set(value) {
            view.enabled = value
        }

    var hidden: Boolean?
        get() = view.hidden
        set(value) {
            view.hidden = value
        }

    var label: String?
        get() = view.label
        set(value) {
            view.label = value
        }

}