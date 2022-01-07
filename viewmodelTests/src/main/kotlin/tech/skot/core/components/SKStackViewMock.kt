package tech.skot.core.components

open class SKStackViewMock: SKComponentViewMock(), SKStackVC {
    override var state: SKStackVC.State = SKStackVC.State(emptyList())
}