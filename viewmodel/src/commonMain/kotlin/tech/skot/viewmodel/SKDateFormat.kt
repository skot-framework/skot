package tech.skot.viewmodel

import com.soywiz.klock.DateTimeTz

expect class SKDateFormat(pattern:String) {
    fun formatLocale(date:DateTimeTz):String
}