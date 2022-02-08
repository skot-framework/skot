package tech.skot.core

object SKLog:SKLogger("SKLog") {
    fun network(message:String) {
        SKNetworkLog.d(message)
    }
}

object SKNetworkLog:SKLogger("SKNetwork")