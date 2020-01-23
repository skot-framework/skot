package tech.skot.view.frame

import tech.skot.contract.components.ScreenView
import tech.skot.view.component.ComponentObserverInterface

interface FrameObserverInterface : ComponentObserverInterface {
    fun setScreen(screen: ScreenView)
}