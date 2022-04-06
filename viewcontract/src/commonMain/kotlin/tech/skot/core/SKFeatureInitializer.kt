package tech.skot.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SKFeatureInitializer(
    val initialize: suspend () -> Unit,
    val onDeepLink: ((uri: SKUri, fromWebView:Boolean) -> Boolean)?,
    val start: suspend () -> Unit,
    val resetToRoot:()->Unit
) {

    private var done = false
    private val initializeMutex = Mutex()
    suspend fun initializeIfNeeded(uri: SKUri?) {
        if (!done && onDeepLink != null) {
            initializeMutex.withLock {
                if (!done) {
                    done = true
                    initialize()
                    start()
                    uri?.let {
                        onDeepLink.invoke(uri, false)
                    }
                }
            }
        }
    }
}