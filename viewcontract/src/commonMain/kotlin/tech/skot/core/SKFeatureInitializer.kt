package tech.skot.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SKFeatureInitializer(val initialize:suspend ()->Unit) {

    private var done = false
    private val initializeMutex = Mutex()
    suspend fun initializeIfNeeded() {
        if (!done) {
            initializeMutex.withLock {
                if (!done) {
                    initialize()
                }
            }
        }
    }
}