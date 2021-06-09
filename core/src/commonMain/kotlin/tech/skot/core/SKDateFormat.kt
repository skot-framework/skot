package tech.skot.core

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

expect class SKDateFormat(pattern:String) {
    fun format(instant: Instant):String
    fun format(localDateTime: LocalDateTime):String
    fun parse(str:String): Instant
}