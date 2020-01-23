package tech.skot.contract.components

interface ScreenObserverInterface : ComponentObserverInterface {
    fun onLoading(state: Boolean)
    fun onOnBack(state: (() -> Unit)?)
    fun openScreen(screen: ScreenView)
}