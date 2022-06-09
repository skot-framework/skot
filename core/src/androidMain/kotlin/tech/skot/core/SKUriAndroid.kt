package tech.skot.core

import android.net.Uri


fun Uri.toSKUri(url:String? = null): SKUri? =
    try {
        SKUri(
            scheme = scheme,
            host = host,
            pathSegments = pathSegments,
            parameters = queryParameterNames.map {
                it to getQueryParameters(it)
            }.toMap(),
            url ?: toString()
        )
    } catch (ex: Exception) {
        SKLog.e(ex, "Erreur au parse d'une Uri ${toString()}")
        null
    }

actual fun String.toSKUri(): SKUri? = try {
    Uri.parse(this).toSKUri(this)
} catch (ex: Exception) {
    null
}