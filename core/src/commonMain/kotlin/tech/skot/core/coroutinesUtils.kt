package tech.skot.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

suspend fun <R> atomic(block: suspend CoroutineScope.() -> R): R =
    GlobalScope.async(block = block)
        .await()


suspend fun parrallel(coroutineScope: CoroutineScope, vararg blocks: suspend () -> Unit) {
    val deffereds =
        blocks.map {
            coroutineScope.async {
                it()
            }
        }
    deffereds.forEach { it.await() }
}
