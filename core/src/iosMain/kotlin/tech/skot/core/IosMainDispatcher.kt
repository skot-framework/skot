package tech.skot.core

import kotlinx.coroutines.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext


@OptIn(InternalCoroutinesApi::class)
actual fun mainDispatcher(): CoroutineContext = object : CoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) { block.run() }
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        TODO("Not yet implemented")
    }

}