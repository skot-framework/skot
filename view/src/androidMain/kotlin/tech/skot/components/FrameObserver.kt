package tech.skot.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import tech.skot.view.Container
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment
import kotlin.reflect.KClass

open class FrameObserver(
        private val fragmentClass: KClass<out SKFragment>,
        container: Container<out SKActivity, out SKFragment>,
        private val idFrameLayout: Int,
        val customizeTransaction: (FragmentTransaction.() -> Unit) = { setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) }
) : ComponentObserver<SKActivity, SKFragment>(container), FrameObserverInterface {
    override fun setScreen(screen: ScreenView) {
        fragmentManager.apply {
            val currentFragment = findFragmentById(idFrameLayout)
            if (currentFragment == null || currentFragment.arguments?.getLong("") != screen.key) {
                val trans = beginTransaction()
                trans.customizeTransaction()
                trans.replace(idFrameLayout, SKFragment.getInstance(screen.key, fragmentClass) as Fragment)
                        .commitAllowingStateLoss()
            }
        }
    }
}