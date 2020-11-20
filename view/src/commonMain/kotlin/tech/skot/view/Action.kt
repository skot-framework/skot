package tech.skot.view

import tech.skot.components.ScreenView

open class Action
data class OpenScreen(val screen: ScreenView):Action()
data class ShowBottomSheetDialog(val screen: ScreenView):Action()
object Dismiss:Action()