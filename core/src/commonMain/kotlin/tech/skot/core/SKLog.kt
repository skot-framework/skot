package tech.skot.core

expect object SKLog {
    fun d(message: String)
    fun i(message:String)
    fun w(message:String)
    fun e(e:Throwable, message:String? = null)
    fun network(message:String)
}
