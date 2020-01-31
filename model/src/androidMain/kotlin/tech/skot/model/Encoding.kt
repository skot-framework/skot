package tech.skot.model

import android.util.Base64

actual fun decodeBase64(str:String):String {
    return String(Base64.decode(str, Base64.DEFAULT))
}