package tech.skot.core

actual object SKLog {
    actual fun d(message: String) {
        System.out.println(message)
    }

    actual fun i(message: String) {
        System.out.println(message)
    }

    actual fun w(message: String) {
        System.out.println(message)
    }

    actual fun e(message: String?, e: Throwable) {
        System.out.println(message)
    }

    actual fun network(message:String) {
        System.out.println(message)
    }

}
