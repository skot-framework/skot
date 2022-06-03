package tech.skot.core

expect fun Double.asPrice(
    isoCurrency: String,
    maximumFractionDigits: Int = 2,
    minimumFractionDigits: Int = 0
): String

fun Double.asPriceTwoOrNoDigits(isoCurrency: String) =
    if ((this - toInt()) == 0.0) asPrice(isoCurrency) else asPrice(
        isoCurrency,
        minimumFractionDigits = 2
    )