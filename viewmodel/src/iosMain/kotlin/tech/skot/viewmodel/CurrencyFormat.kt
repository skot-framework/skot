package tech.skot.viewmodel

import platform.Foundation.NSDate

//import java.text.DecimalFormat
//import java.text.NumberFormat
//import java.util.*
//
//private val mapFromatter = mutableMapOf<String, NumberFormat>()
//
//actual fun Double.asPrice(isoCurrency: String) = getFormatter(isoCurrency, Locale.getDefault()).format(this)
//
//private fun getFormatter(isoCurrency: String, locale: Locale): NumberFormat {
//    val key = "${isoCurrency}_${locale.country}"
//    return mapFromatter[key] ?: DecimalFormat.getCurrencyInstance(locale).apply {
//        maximumFractionDigits = 2
//        minimumFractionDigits = 0
//        currency = Currency.getInstance(isoCurrency)
//        mapFromatter[key] = this
//    }
//}

//private val mapFromatter = mutableMapOf<String, NumberFormatter>()

actual fun Double.asPrice(isoCurrency: String) = ""

//private fun getFormatter(isoCurrency: String, locale:NSLocale): NumberFormatter {
//    val key = "${isoCurrency}_${locale}"
//    return mapFromatter[key] ?: DecimalFormat.getCurrencyInstance(locale).apply {
//        maximumFractionDigits = 2
//        minimumFractionDigits = 0
//        currency = Currency.getInstance(isoCurrency)
//        mapFromatter[key] = this
//    }
//}