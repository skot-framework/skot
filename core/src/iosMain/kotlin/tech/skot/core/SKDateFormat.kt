package tech.skot.core

import kotlinx.datetime.*

actual class SKDateFormat actual constructor(pattern: String) {

    actual fun format(instant: Instant): String {
        TODO("SKDateFormat format instant not implemented")
    }
    actual fun format(localDateTime: LocalDateTime):String {
        return format(localDateTime.toInstant(TimeZone.currentSystemDefault()))
    }

    actual fun format(localDate: LocalDate):String {
        TODO("SKDateFormat format localDate not implemented")
    }

    actual fun parse(str: String): Instant {
        TODO("SKDateFormat parse not implemented")
    }

}