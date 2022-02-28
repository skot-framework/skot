package tech.skot.core

open class SKLogger(val tag:String) {

    var enabled = true

    fun d(message: String) {
        if (enabled) {
            SKLogPrinter.d(tag, message)
        }
    }
    fun i(message:String) {
        if (enabled) {
            SKLogPrinter.i(tag, message)
        }
    }
    fun w(message:String) {
        if (enabled) {
            SKLogPrinter.w(tag, message)
        }
    }
    fun e(e:Throwable, message:String? = null) {
        if (enabled) {
            SKLogPrinter.e(e, tag, message)
        }
    }
}