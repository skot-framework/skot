package tech.skot.viewmodel

import tech.skot.contract.viewcontract.FrameView
import tech.skot.contract.viewcontract.ScreenView

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