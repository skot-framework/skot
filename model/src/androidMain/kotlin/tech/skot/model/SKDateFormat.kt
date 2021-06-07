package tech.skot.model

import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.*

actual class SKDateFormat actual constructor(pattern: String) {

    private val sdf = SimpleDateFormat(pattern)

    actual fun format(instant: Instant): String {
        return sdf.format(Date(instant.toEpochMilliseconds()))
    }

    actual fun parse(str: String): Instant {
        return Instant.fromEpochMilliseconds(sdf.parse(str).time)
    }
}