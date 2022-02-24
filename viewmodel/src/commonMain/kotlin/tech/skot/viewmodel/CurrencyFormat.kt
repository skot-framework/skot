package tech.skot.viewmodel

expect fun Double.asPrice(isoCurrency: String, maximumFractionDigits : Int = 2, minimumFractionDigits : Int = 0): String
