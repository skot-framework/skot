package tech.skot.view.live

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class SKLifecycleOwner(baseLifecycle: Lifecycle): LifecycleOwner {
    val skLifecycle = SKLifecycle(baseLifecycle)
    override fun getLifecycle() = skLifecycle
}