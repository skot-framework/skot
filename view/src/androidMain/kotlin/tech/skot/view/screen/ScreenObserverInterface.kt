package tech.skot.view.screen

import tech.skot.contract.components.ScreenView
import tech.skot.view.component.ComponentObserverInterface

interface ScreenObserverInterface : ComponentObserverInterface {
    fun onLoading(state:Boolean)
    fun onOnBack(state:(()->Unit)?)
    fun openScreen(screen: ScreenView)
}