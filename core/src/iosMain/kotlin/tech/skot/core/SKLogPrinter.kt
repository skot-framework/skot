package tech.skot.core

actual object SKLogPrinter {
    actual fun d(tag:String, message: String) {
        println("$tag d ---$message")
    }

    actual fun i(tag:String, message: String) {
        println("$tag i ---$message")
    }

    actual fun v(tag:String, message: String) {
        println("$tag v ---$message")
    }

    actual fun w(tag:String, message: String) {
        println("$tag w ---$message")
    }

    actual fun e(e:Throwable, tag:String, message:String?) {
        println("$tag e ---${message ?: e.message ?: ""}")
        e.printStackTrace()
    }
}
