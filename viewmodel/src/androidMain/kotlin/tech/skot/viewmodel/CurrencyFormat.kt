package tech.skot.viewmodel

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

actual class CurrencyFormat actual constructor(iso:String) {
    private val formatter = NumberFormat.getCurrencyInstance(getLocalFromISO(iso)).apply {
        (this as? DecimalFormat)?.isDecimalSeparatorAlwaysShown = false
    }

    actual fun format(double: Double) = formatter.format(double)

    private fun getLocalFromISO(iso4217code: String): Locale? {
        var toReturn: Locale? = null
        for (locale in NumberFormat.getAvailableLocales()) {
            val code = NumberFormat.getCurrencyInstance(locale).currency.currencyCode
            if (iso4217code == code) {
                toReturn = locale
                break
            }
        }
        return toReturn
    }
}