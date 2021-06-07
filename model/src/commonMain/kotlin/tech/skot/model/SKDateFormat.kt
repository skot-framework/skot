package tech.skot.model

import kotlinx.datetime.Instant

expect class SKDateFormat(pattern:String) {
    fun format(instant:Instant):String
    fun parse(str:String):Instant
}