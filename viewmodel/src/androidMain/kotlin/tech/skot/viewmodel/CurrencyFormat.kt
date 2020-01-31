package tech.skot.viewmodel

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

actual class CurrencyFormat actual constructor() {
    private val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        (this as? DecimalFormat)?.isDecimalSeparatorAlwaysShown = false
    }

    actual fun format(double: Double) = formatter.format(double)

}