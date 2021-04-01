package tech.skot.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

suspend fun atomic( block: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(block = block)
            .join()
}
