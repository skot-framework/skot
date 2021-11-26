package tech.skot.viewmodel

import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumber


private val mapFromatter = mutableMapOf<String, NSNumberFormatter>()

private fun getFormatter(isoCurrency: String): NSNumberFormatter {
    return mapFromatter[isoCurrency] ?: NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        currencyCode = isoCurrency
        mapFromatter[isoCurrency] = this
    }
}


actual fun Double.asPrice(isoCurrency: String):String {
    return getFormatter(isoCurrency).stringFromNumber(NSNumber(this)) ?: ""
}