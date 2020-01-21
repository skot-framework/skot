package tech.skot.view.live

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual fun runOnMainThread(lambda: () -> Unit) {
//    ArchTaskExecutor.getInstance().postToMainThread { lambda() }
    CoroutineScope(Dispatchers.Main).launch {
        lambda()
    }
}