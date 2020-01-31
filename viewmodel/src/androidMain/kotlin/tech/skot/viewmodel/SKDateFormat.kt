package tech.skot.viewmodel

import com.soywiz.klock.DateTimeTz
import java.text.SimpleDateFormat
import java.util.*

actual class SKDateFormat actual constructor(pattern:String) {
    private val sdf = SimpleDateFormat(pattern)
    actual fun formatLocale(date: DateTimeTz): String {
        return sdf.format(Date(date.utc.unixMillisLong))
    }

}