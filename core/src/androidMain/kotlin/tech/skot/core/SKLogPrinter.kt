package tech.skot.core

import timber.log.Timber

actual object SKLogPrinter {
    actual fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    actual fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    actual fun v(tag: String, message: String) {
        Timber.tag(tag).v(message)
    }

    actual fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    actual fun e(e: Throwable, tag: String, message: String?) {
        Timber.tag(tag).e(Throwable(e), message)
    }

}
