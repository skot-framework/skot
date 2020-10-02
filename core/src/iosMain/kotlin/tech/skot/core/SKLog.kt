package tech.skot.core

actual object SKLog {
    actual fun d(message: String) {
        println("SKLog d ---$message")
    }

    actual fun i(message: String) {
        println("SKLog i ---$message")
    }

    actual fun w(message: String) {
        println("SKLog w ---$message")
    }

    actual fun e(message:String?, e:Throwable) {
        println("SKLog e ---${message ?: e.message ?: ""}")
        e.printStackTrace()
    }

}
