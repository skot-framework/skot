package tech.skot.components

import tech.skot.contract.components.FrameView
import tech.skot.contract.components.ScreenView

abstract class Frame : Component<FrameView>() {

    var screen: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            view.screen = value?.view
        }

    override fun onRemove() {
        screen?.onRemove()
        super.onRemove()
    }

}