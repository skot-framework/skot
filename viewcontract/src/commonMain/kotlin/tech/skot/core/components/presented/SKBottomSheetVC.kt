package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKScreenVC

@SKLayoutNo
interface SKBottomSheetVC:SKComponentVC {

    data class Shown(val screen:SKScreenVC)

    var state :Shown?
}