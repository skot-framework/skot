package tech.skot.model

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SKState {
    val toto:String= "sdf"
    private var restored: Boolean = false
    private val restoreMutex = Mutex()

    abstract suspend fun restore()

    protected suspend fun <R> withRestored(block: suspend () -> R): R {
        if (!restored) {
            restoreMutex.withLock {
                if (!restored) {
                    restore()
                    restored = true
                }
            }
        }
        return block.invoke()
    }

    inner class SKSubState<S:SKState> {

        private var field:S? = null

        fun setValue(value:S?) {
            field = value
        }

        suspend operator fun invoke() = withRestored {
            field
        }
    }
}

