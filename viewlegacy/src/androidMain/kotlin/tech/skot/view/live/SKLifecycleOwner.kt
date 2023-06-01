package tech.skot.view.live

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class SKLifecycleOwner(baseLifecycle: Lifecycle): LifecycleOwner {
    override val lifecycle: SKLifecycle = SKLifecycle(baseLifecycle)
}