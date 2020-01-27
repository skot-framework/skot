package tech.skot.components

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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


    protected fun launchWithLoadingAndError(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit) {
        launch(context, start) {
            view.loading = true
            try {
                block()
            } catch (ex: Exception) {
                if (!(ex is CancellationException)) {
                    treatError(ex)

                }
            }
            view.loading = false
        }
    }

    open protected fun treatError(ex: Exception) {
        logE(ex.message ?: "Erreur inconnue", ex)
    }

}
