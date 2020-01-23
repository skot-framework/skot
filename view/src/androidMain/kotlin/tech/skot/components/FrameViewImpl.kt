package tech.skot.components

import tech.skot.contract.components.FrameObserverInterface
import tech.skot.contract.components.FrameView
import tech.skot.contract.components.ScreenView
import tech.skot.view.Action
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment

class FrameViewImpl : ComponentViewImpl<FrameObserverInterface, SKActivity, SKFragment>(), FrameView {

    data class SetScreen(val screen: ScreenView) : Action()

    override var screen: ScreenView? = null
        set(newVal) {
            field = newVal
            newVal?.let { messages.post(SetScreen(newVal)) }
        }

    override fun treatAction(action: Action, observer: FrameObserverInterface) {
        when (action) {
            is SetScreen -> observer.setScreen(action.screen)
            else -> super.treatAction(action, observer)
        }

    }
}