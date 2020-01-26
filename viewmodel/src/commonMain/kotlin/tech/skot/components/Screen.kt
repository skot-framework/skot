package tech.skot.components

abstract class Screen<V : ScreenView> : Component<V>() {

    companion object {
        private var root: Screen<out ScreenView>? = null
            set(value) {
                field?.let { oldRootScreen ->
                    if (value != null) {
                        oldRootScreen.view.openScreenWillFinish(value.view)
                    }
                    oldRootScreen.onRemove()
                }
                field = value
            }
    }

    override fun onRemove() {
        onTop?.onRemove()
        super.onRemove()
    }

    var onTop: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            value?._parent = this
            view.onTop = value?.view
        }

    private var _parent: Screen<out ScreenView>? = null
    val parent: Screen<out ScreenView>?
        get() = _parent

    fun setAsRoot() {
        root = this
    }
}
