package tech.skot.view

import androidx.lifecycle.LifecycleOwner

class Container<A : SKActivity, F : SKFragment>(val activity: A, val fragment: F?) {
    val lifecycleOwner: LifecycleOwner = fragment ?: activity
}