package tech.skot.core

import android.net.Uri

fun Uri.toSKUri(): SKUri = SKUri(
    scheme = scheme,
    host = host,
    pathSegments = pathSegments,
    parameters = queryParameterNames.map {
        it to getQueryParameters(it)
    }.toMap()
)