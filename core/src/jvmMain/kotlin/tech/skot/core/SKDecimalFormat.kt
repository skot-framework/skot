package tech.skot.core

import java.text.DecimalFormat


actual class SKDecimalFormat actual constructor(pattern: String) {
    private val df = DecimalFormat(pattern)
    actual fun format(number: Number): String {
        return df.format(number)
    }

    actual fun parse(str: String): Number? {
        return df.parse(str)
    }
}