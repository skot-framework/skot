package tech.skot.core.components

class SKFrameViewMock(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?): SKComponentViewMock(), SKFrameVC {
    override val screens: Set<SKScreenVC> = screens
    override var screen: SKScreenVC? = screenInitial
}