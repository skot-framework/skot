package tech.skot.core.components

import kotlinx.coroutines.*
import tech.skot.core.Poker
import tech.skot.core.SKLog
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class Component<out V : ComponentVC>: CoroutineScope {
    abstract val view: V

    protected val job = SupervisorJob()
    override val coroutineContext = CoroutineScope(Dispatchers.Main + job).coroutineContext

    open fun onRemove() {
        //remove des Pokers
        removeObservers.forEach { it.invoke() }
        job.cancel()
    }


    private val noCrashExceptionHandler = CoroutineExceptionHandler { _, e ->
        SKLog.e("launchNoCrash", e)
    }

    protected fun launchNoCrash(start: CoroutineStart = CoroutineStart.DEFAULT,
                                block: suspend CoroutineScope.() -> Unit): Job =
            launch(noCrashExceptionHandler, start, block)


    private var removeObservers: MutableSet<() -> Unit> = mutableSetOf()
    private fun addRemoveObserver(observer: () -> Unit) {
        removeObservers.add(observer)
    }

    private fun removeRemoveObserver(observer: () -> Unit) {
        removeObservers.remove(observer)
    }

    fun observe(poker: Poker, onPoke: () -> Unit) {
        poker.addObserver(onPoke)
        addRemoveObserver { poker.removeObserver(onPoke) }
    }

    fun logD(message: Any?) {
        SKLog.d("${this::class.simpleName} -- $message")
    }

    fun logE(message: Any? = "", throwable: Throwable) {
        SKLog.e("${this::class.simpleName} -- $message", throwable)
    }

}
