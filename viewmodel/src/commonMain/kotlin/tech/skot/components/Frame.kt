package tech.skot.components

abstract class Frame<V:FrameView> : Component<V>(), ScreenParent {

    var screen: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            view.screen = value?.view
            value?._parent = this
        }

    override fun remove(aScreen: Screen<*>) {
        throw IllegalStateException("This screen's parent is a Frame, you can't finish it like this")
    }

    override fun onRemove() {
        screen?.onRemove()
        super.onRemove()
    }

}