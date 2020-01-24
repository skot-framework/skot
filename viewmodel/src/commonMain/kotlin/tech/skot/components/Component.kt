package tech.skot.components

import kotlinx.coroutines.*
import tech.skot.contract.modelcontract.Poker
import tech.skot.core.SKLog
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class Component<out V : ComponentView> {
    abstract val view: V

    protected val job = SupervisorJob()
    protected val mainScope = CoroutineScope(Dispatchers.Main + job)

    open fun onRemove() {
        //remove des Pokers
        removeObservers.forEach { it.invoke() }
        view.onRemove()
        job.cancel()
    }

    protected fun launch(context: CoroutineContext = EmptyCoroutineContext,
                         start: CoroutineStart = CoroutineStart.DEFAULT,
                         block: suspend CoroutineScope.() -> Unit) {
        mainScope.launch(context, start, block)
    }

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

    fun logD(message:String) {
        SKLog.d("${this::class.simpleName} -- $message")
    }

}
