package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.view.Action

abstract class BaseFrameViewImpl<A : AppCompatActivity, F : Fragment, B: ViewBinding> : ComponentViewImpl<A, F, B>(), BaseFrameView {

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
            if (currentFragment == null || currentFragment.arguments?.getLong(ScreenViewImpl.SK_EXTRA_VIEW_KEY) != screen.key) {
                val trans = beginTransaction()
                trans.customizeTransaction()
                trans.replace(idFrameLayout, ScreenViewImpl.getInstance(screen.key).createFragmentWithKey())
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