package tech.skot.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SKFeatureInitializer(
    val onDeepLink: (pathSegments: List<String>) -> Unit,
    val initialize: suspend () -> Unit
) {

    private var done = false
    private val initializeMutex = Mutex()
    suspend fun initializeIfNeeded(deepLinkPathSegments:List<String>?) {
        if (!done) {
            initializeMutex.withLock {
                if (!done) {
                    deepLinkPathSegments?.let { onDeepLink(it) }
                    initialize()
                    done = true
                }
            }
        }
    }
}