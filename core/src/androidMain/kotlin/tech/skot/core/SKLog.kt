package tech.skot.core

import timber.log.Timber

actual object SKLog {
    actual fun d(message: String) {
        Timber.d(message)
    }

    actual fun i(message: String) {
        Timber.i(message)
    }

    actual fun w(message: String) {
        Timber.w(message)
    }

    actual fun e(e: Throwable, message: String?) {
        Timber.e(Throwable(e), message)
    }

    actual fun network(message:String) {
        Timber.tag("SKNetwork").d(message)
    }

}
