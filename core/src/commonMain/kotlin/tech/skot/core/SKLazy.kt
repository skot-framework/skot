package tech.skot.core

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SKLazy<A : Any>(private val creator: suspend () -> A) {

    private val mutex = Mutex()
    private var _value: A? = null
    suspend fun get(): A {
        return _value ?: mutex.withLock {
            _value ?: creator().apply {
                _value = this
            }

        }

    }

}