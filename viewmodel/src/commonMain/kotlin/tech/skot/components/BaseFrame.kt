package tech.skot.components

abstract class BaseFrame<V:BaseFrameView> : Component<V>() {

    var screen: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            view.screen = value?.view
        }

    override fun onRemove() {
        screen?.onRemove()
        super.onRemove()
    }

}