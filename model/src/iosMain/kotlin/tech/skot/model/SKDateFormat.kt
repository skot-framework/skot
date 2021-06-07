package tech.skot.model

import kotlinx.datetime.Instant

actual class SKDateFormat actual constructor(pattern: String) {

    actual fun format(instant: Instant): String {
        TODO("SKDateFormat format not implemented")
    }

    actual fun parse(str: String): Instant {
        TODO("SKDateFormat parse not implemented")
    }
}