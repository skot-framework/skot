package tech.skot.model

import android.util.Base64

actual fun decodeBase64(str: String): String {
    return String(Base64.decode(str, Base64.DEFAULT))
}

actual fun encodeBase64(str: String) = Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP or Base64.URL_SAFE)
