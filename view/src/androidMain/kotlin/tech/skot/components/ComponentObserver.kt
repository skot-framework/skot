package tech.skot.components

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import tech.skot.view.Container
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment

abstract class ComponentObserver<A : SKActivity, F : SKFragment>(
        protected val container: Container< out A, out F>
) : ComponentObserverInterface {
    val activity: A = container.activity
    val fragment: F? = container.fragment
    val context: Context = container.activity.activity
    val fragmentManager =
            container.fragment?.fragment?.childFragmentManager ?: container.activity.activity.supportFragmentManager
    val lifecycleOwner: LifecycleOwner = fragment?.fragment ?: activity?.activity

    override fun onRemove() {
    }
}