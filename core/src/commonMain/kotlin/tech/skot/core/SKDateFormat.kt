package tech.skot.core

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect class SKDateFormat(pattern: String) {
    fun format(instant: Instant): String
    fun format(localDateTime: LocalDateTime): String
    fun format(localDate: LocalDate): String
    fun parse(str: String): Instant
}

fun LocalDate.toLocalDateTime() = LocalDateTime(
    year = year,
    month = month,
    dayOfMonth = dayOfMonth,
    hour = 0,
    minute = 0,
    second = 0,
    nanosecond = 0
)