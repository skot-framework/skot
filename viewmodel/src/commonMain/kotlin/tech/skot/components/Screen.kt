package tech.skot.components

abstract class Screen<V : ScreenView> : Component<V>() {

    override fun onRemove() {
        onTop?.onRemove()
        super.onRemove()
    }

    var onTop: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            view.onTop = value?.view
        }
}