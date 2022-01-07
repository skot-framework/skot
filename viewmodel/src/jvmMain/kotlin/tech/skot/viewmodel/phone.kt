package tech.skot.viewmodel


actual fun String?.formatPhoneNumber():String? {
    return this?.uppercase()
}