package tech.skot.components

import tech.skot.contract.Private

interface FrameView : ComponentView {
    @Private
    var screen: ScreenView?
}