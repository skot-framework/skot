package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock

class SKSnackBarViewMock: SKComponentViewMock(), SKSnackBarVC {
    override var state: SKSnackBarVC.Shown? = null

}