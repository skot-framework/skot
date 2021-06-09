package tech.skot.core

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.*

actual class SKDateFormat actual constructor(pattern: String) {

    private val sdf = SimpleDateFormat(pattern)

    actual fun format(instant: Instant): String {
        return sdf.format(Date(instant.toEpochMilliseconds()))
    }

    actual fun format(localDateTime: LocalDateTime):String {
        return format(localDateTime.toInstant(TimeZone.currentSystemDefault()))
    }
    actual fun parse(str: String): Instant {
        return Instant.fromEpochMilliseconds(sdf.parse(str).time)
    }
}