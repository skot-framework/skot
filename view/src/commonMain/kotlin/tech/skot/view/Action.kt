package tech.skot.view

import tech.skot.contract.viewcontract.ScreenView

open class Action
object Remove:Action()
data class OpenScreen(val screen: ScreenView):Action()