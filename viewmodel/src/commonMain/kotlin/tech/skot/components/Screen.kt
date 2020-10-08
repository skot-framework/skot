package tech.skot.components

import kotlinx.coroutines.*
import tech.skot.core.MutablePoker
import tech.skot.core.Poker
import tech.skot.core.SKLog
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface ScreenParent {
    fun remove(aScreen: Screen<*>)
}
var rootScreen: Screen<out ScreenView>? = null
    set(value) {
        SKLog.d("---- set root oldValue: ${field?.let { it::class.simpleName }} new Value: ${value?.let { it::class.simpleName }} ")
        field?.let { oldRootScreen ->
            if (value != null) {
                oldRootScreen.view.openScreenWillFinish(value.view)
            }
            oldRootScreen.onRemove()
        }
        field = value
    }

abstract class Screen<V : ScreenView> : Component<V>(), ScreenParent {

    /*companion object {
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

    }*/

    fun push(screen: Screen<*>) {
        parent?.let {
            if (it is Stack<*>) {
                it.push(screen)
            } else {
                SKLog.e("This screen is not in a Stack !!! will put on Top instead", IllegalStateException("Screen ${this::class.simpleName}is not in a Stack !!!"))
                onTop = screen
            }
        }

    }

    fun pushOrPutOnTop(screen: Screen<*>) {
        val currentParent = parent
        when (currentParent) {
            is Stack<*> -> currentParent.push(screen)
            else -> onTop = screen
        }
    }

    fun showBottomSheetDialog(screen: Screen<*>) {
        view.showBottomSheetDialog(screen.view)
    }

    fun replaceWith(screen: Screen<*>) {
        parent?.let { currenParent ->
            when {
                currenParent == null -> screen.setAsRoot()
                currenParent is Screen<*> -> currenParent.onTop = screen
                currenParent is Frame<*> -> currenParent.screen = screen
                currenParent is Stack<*> -> {
                    currenParent.popScreen()
                    currenParent.push(screen)
                }
                currenParent is FrameKeepingScreens<*> -> currenParent.screen = screen
            }
        }

    }

    fun finish() {
        parent.let {
            if (it != null) {
                it.remove(this)
            } else {
                onRemove()
            }
        }
    }

    fun dismiss() {
        view.dismiss()
    }


    override fun remove(aScreen: Screen<*>) {
        onTop = null
    }

    override fun onRemove() {
        onTop?.onRemove()
        super.onRemove()
    }

    private val _ontopChanged = MutablePoker()
    protected val onTopChanged: Poker = _ontopChanged

    var onTop: Screen<out ScreenView>? = null
        set(value) {
            field?.onRemove()
            field = value
            value?._parent = this
            view.onTop = value?.view
            _ontopChanged.poke()
        }

    internal var _parent: ScreenParent? = null
    val parent: ScreenParent?
        get() = _parent

    fun setAsRoot() {
        rootScreen = this
    }

    fun setAsInitial(): Screen<V> {
        setAsRoot()
        return this
    }

    private var loadingCounter = 0
        set(value) {
            field = value
            view.loading = value > 0
        }

    fun launchWithLoadingAndError(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            defaultErrorMessage: String? = null,
            block: suspend CoroutineScope.() -> Unit): Job =
            launch(context, start) {
                loadingCounter++
                try {
                    block()
                } catch (ex: Exception) {
                    if (ex !is CancellationException) {
                        treatError(ex)
                    }
                } finally {
                    loadingCounter--
                }

            }


    protected suspend fun CoroutineScope.parrallel(vararg blocks: suspend CoroutineScope.() -> Unit) {
        val deffereds =
                blocks.map {
                    async {
                        try {
                            it()
                        } catch (ex: Exception) {
                            if (ex !is CancellationException) {
                                treatError(ex)
                            }
                        }
                    }
                }

        deffereds.forEach { it.await() }
    }


    open fun treatError(ex: Exception, defaultErrorMessage: String? = null) {
        logE(ex.message ?: "Erreur inconnue", ex)
    }

}
