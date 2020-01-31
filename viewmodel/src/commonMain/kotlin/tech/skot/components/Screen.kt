package tech.skot.components

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import tech.skot.core.SKLog
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

                value?.let { screenOnTop = it }
            }

        var screenOnTop:Screen<*>? = null
    }

    fun finish() {
        if (root == this) {
            onRemove()
        }
        else {
            parent?.onTop = null
        }
    }

    override fun onRemove() {
        onTop?.onRemove()
        super.onRemove()
    }


    var onTop: Screen<out ScreenView>? = null
        set(value) {
            if (value != null) {
                screenOnTop = value
            }
            else {
                screenOnTop = this
            }

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

    fun setAsInitial():Screen<V> {
        setAsRoot()
        return this
    }

    private var loadingCounter = 0
    set(value) {
        field = value
        view.loading = value > 0
    }

    protected fun launchWithLoadingAndError(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit) {
        launch(context, start) {
            loadingCounter++
            try {
                block()
            } catch (ex: Exception) {
                if (!(ex is CancellationException)) {
                    treatError(ex)
                }
            } finally {
                loadingCounter--
            }

        }
    }

    open protected fun treatError(ex: Exception) {
        logE(ex.message ?: "Erreur inconnue", ex)
    }

}
