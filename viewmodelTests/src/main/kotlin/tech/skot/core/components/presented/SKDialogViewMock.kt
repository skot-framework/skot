package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock

class SKDialogViewMock: SKComponentViewMock(), SKDialogVC {
    override var state: SKDialogVC.Shown? = null
}