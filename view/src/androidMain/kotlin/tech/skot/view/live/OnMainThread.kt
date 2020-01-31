package tech.skot.view.live

import androidx.annotation.UiThread

@UiThread
actual fun runOnMainThread(lambda: () -> Unit) {
//    CoroutineScope(Dispatchers.Main).launch {
    lambda()
//    }
}