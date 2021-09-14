package tech.skot.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SKFeatureInitializer(
    val onDeepLink: ((uri: SKUri) -> Unit)?,
    val initialize: suspend () -> Unit
) {

    private var done = false
    private val initializeMutex = Mutex()
    suspend fun initializeIfNeeded(uri: SKUri?) {
        if (!done && onDeepLink != null) {
            initializeMutex.withLock {
                if (!done) {
                    uri?.let { onDeepLink.invoke(uri) }
                    initialize()
                    done = true
                }
            }
        }
    }
}