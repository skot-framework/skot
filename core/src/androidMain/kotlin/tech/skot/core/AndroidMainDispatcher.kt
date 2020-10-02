package tech.skot.core

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual fun mainDispatcher():CoroutineContext = Dispatchers.Main