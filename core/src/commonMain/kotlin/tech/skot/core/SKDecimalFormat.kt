package tech.skot.core

/**
 * Format a decimal using pattern : Unicode Technical Standard #35
 * https://unicode.org/reports/tr35/tr35-numbers.html#Number_Format_Patterns
 */
expect class SKDecimalFormat(pattern: String) {
    fun format(number: Number): String
    fun parse(str: String): Number?
}