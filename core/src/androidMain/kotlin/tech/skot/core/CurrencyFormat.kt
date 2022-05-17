package tech.skot.core

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

private val mapFormatter = mutableMapOf<String, NumberFormat>()

actual fun Double.asPrice(
    isoCurrency: String,
    maximumFractionDigits: Int,
    minimumFractionDigits: Int
) = getFormatter(
    isoCurrency,
    Locale.getDefault(),
    maximumFractionDigits,
    minimumFractionDigits
).format(this)

private fun getFormatter(
    isoCurrency: String,
    locale: Locale,
    maximumFractionDigits: Int,
    minimumFractionDigits: Int
): NumberFormat {
    val key = "${isoCurrency}_${locale.country}_${maximumFractionDigits}_${minimumFractionDigits}"
    return mapFormatter[key] ?: DecimalFormat.getCurrencyInstance(locale).apply {
        this.maximumFractionDigits = maximumFractionDigits
        this.minimumFractionDigits = minimumFractionDigits
        currency = Currency.getInstance(isoCurrency)
        mapFormatter[key] = this
    }
}

fun skResetCurrencyFormatters() {
    mapFormatter.clear()
}