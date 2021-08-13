package tech.skot.core

import timber.log.Timber

actual object SKLog {
    actual fun d(message: String) {
        Timber.tag("SKLog").d(message)
    }

    actual fun i(message: String) {
        Timber.tag("SKLog").i(message)
    }

    actual fun w(message: String) {
        Timber.tag("SKLog").w(message)
    }

    actual fun e(e: Throwable, message: String?) {
        Timber.tag("SKLog").e(Throwable(e), message)
    }

    actual fun network(message:String) {
        Timber.tag("SKNetwork").d(message)
    }

}
