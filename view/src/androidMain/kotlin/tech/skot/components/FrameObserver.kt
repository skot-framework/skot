package tech.skot.components

import tech.skot.contract.components.FrameObserverInterface
import tech.skot.contract.components.ScreenView
import tech.skot.view.Container
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment
import tech.skot.view.SKFragmentImpl

class FrameObserver(
        container: Container<SKActivity, SKFragment>,
        val idFrameLayout: Int
) : ComponentObserver<SKActivity, SKFragment>(container), FrameObserverInterface {
    override fun setScreen(screen: ScreenView) {
        fragmentManager.apply {
            val currentFragment = findFragmentById(idFrameLayout)
            if (currentFragment == null || currentFragment.arguments?.getLong("") != screen.key) {
                beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(idFrameLayout, SKFragment.getInstance<SKFragmentImpl>(screen.key))
                        .commitAllowingStateLoss()
            }
        }
    }
}