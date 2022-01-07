package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock

class SKBottomSheetViewMock: SKComponentViewMock(), SKBottomSheetVC {
    override var state: SKBottomSheetVC.Shown? = null
}