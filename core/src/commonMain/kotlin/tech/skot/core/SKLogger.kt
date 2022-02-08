package tech.skot.core

open class SKLogger(val tag:String) {

    fun d(message: String) {
        SKLogPrinter.d(tag, message)
    }
    fun i(message:String) {
        SKLogPrinter.i(tag, message)
    }
    fun w(message:String) {
        SKLogPrinter.w(tag, message)
    }
    fun e(e:Throwable, message:String? = null) {
        SKLogPrinter.e(e, tag, message)
    }
}