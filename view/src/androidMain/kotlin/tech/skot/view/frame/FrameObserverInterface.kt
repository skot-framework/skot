package tech.skot.view.frame

import tech.skot.contract.viewcontract.ScreenView
import tech.skot.view.component.ComponentObserverInterface

interface FrameObserverInterface : ComponentObserverInterface {
    fun setScreen(screen: ScreenView)
}