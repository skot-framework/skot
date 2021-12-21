package tech.skot.core

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.text.SimpleDateFormat
import java.util.*

actual class SKDateFormat actual constructor(pattern: String) {

    private val sdf = SimpleDateFormat(pattern)

    actual fun format(instant: Instant): String {
        return sdf.format(Date(instant.toEpochMilliseconds()))
    }

    actual fun format(localDateTime: LocalDateTime): String {
        return format(localDateTime.toInstant(TimeZone.currentSystemDefault()))
    }

    actual fun format(localDate: LocalDate): String {
        return format(localDate.toLocalDateTime())
    }

    actual fun parse(str: String): Instant {
        return Instant.fromEpochMilliseconds(sdf.parse(str).time)
    }
}