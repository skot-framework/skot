package tech.skot.core

val skRegexPlaceholder: Regex by lazy {
    Regex("%[dsS@]")
}

//fun String.skFormat(vararg values: String): String {
//    var current: String = this
//    var currentValueIndex = 0
//    while (current.contains(skRegexPlaceholder)) {
//        if (currentValueIndex < values.size) {
//            current = current.replaceFirst(skRegexPlaceholder, values[currentValueIndex])
//            currentValueIndex++
//        } else {
//            break
//        }
//    }
//    return current
//}

expect fun String.skFormat(vararg values: String): String