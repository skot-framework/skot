package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import tech.skot.view.Action

abstract class BaseFrameViewImpl<A : AppCompatActivity, F : Fragment> : ComponentViewImpl<A, F>(), BaseFrameView {

    data class SetScreen(val screen: ScreenView) : Action()

    override var screen: ScreenView? = null
        set(newVal) {
            field = newVal
            newVal?.let { messages.post(SetScreen(newVal)) }
        }

    abstract val idFrameLayout: Int
    open fun FragmentTransaction.customizeTransaction() {

    }

    private fun setScreenNow(screen: ScreenView) {
        fragmentManager.apply {
            val currentFragment = findFragmentById(idFrameLayout)
            if (currentFragment == null || currentFragment.arguments?.getLong("") != screen.key) {
                val trans = beginTransaction()
                trans.customizeTransaction()
                trans.replace(idFrameLayout, ScreenViewImpl.getInstance(screen.key).createFragment())
                        .commitAllowingStateLoss()
            }
        }
    }

    override fun treatAction(action: Action) {
        when (action) {
            is SetScreen -> setScreenNow(action.screen)
            else -> super.treatAction(action)
        }

    }
}