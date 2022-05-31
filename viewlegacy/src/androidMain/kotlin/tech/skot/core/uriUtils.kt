package tech.skot.core

import android.net.Uri

fun Uri.toSKUri(): SKUri? =
    try {
        SKUri(
            scheme = scheme,
            host = host,
            pathSegments = pathSegments,
            parameters = queryParameterNames.map {
                it to getQueryParameters(it)
            }.toMap()
        )
    }
    catch (ex:Exception) {
        SKLog.e(ex, "Erreur au parse d'une Uri ${toString()}")
        null
    }