package tech.skot.core

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

actual class SKDateFormat actual constructor(pattern: String) {


    actual fun format(instant: Instant): String {
        TODO()
    }

    actual fun format(localDateTime: LocalDateTime): String {
        TODO()
    }

    actual fun format(localDate: LocalDate): String {
        TODO()
    }

    actual fun parse(str: String): Instant {
        TODO()
    }
}