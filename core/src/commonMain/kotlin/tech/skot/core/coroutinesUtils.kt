package tech.skot.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

suspend fun <R> atomic(block: suspend CoroutineScope.() -> R): R =
        GlobalScope.async(block = block)
                .await()

