package tech.skot.view.live

import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual fun runOnMainThread(lambda: () -> Unit) {
    dispatch_async(dispatch_get_main_queue()) { lambda() }
}