package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock

class SKWindowPopupViewMock: SKComponentViewMock(), SKWindowPopupVC {
    override var state: SKWindowPopupVC.Shown? = null
}