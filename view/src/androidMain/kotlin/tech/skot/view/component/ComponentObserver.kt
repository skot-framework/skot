package tech.skot.view.component

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import tech.skot.view.Container
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment

abstract class ComponentObserver<A : SKActivity, F : SKFragment>(
        protected val container: Container<A, F>
) : ComponentObserverInterface {
    val activity: A = container.activity
    val fragment: F? = container.fragment
    val context: Context = container.activity
    val fragmentManager =
            container.fragment?.childFragmentManager ?: container.activity.supportFragmentManager
    val lifecycleOwner: LifecycleOwner = fragment ?: activity

    override fun onRemove() {
    }
}