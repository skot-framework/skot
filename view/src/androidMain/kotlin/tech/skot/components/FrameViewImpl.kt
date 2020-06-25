package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import tech.skot.view.Action

abstract class FrameViewImpl<A : AppCompatActivity, F : Fragment, B : ViewBinding> : ComponentViewImpl<A, F, B>(), FrameView {

    data class SetScreen(val screen: ScreenView) : Action()

    abstract val idFrameLayout: Int

    override var screen: ScreenView? = null
        set(newVal) {
            field = newVal
            newVal?.let { messages.post(SetScreen(newVal)) }
        }

    open fun FragmentTransaction.customizeTransaction() {

    }


    protected open fun setScreenNow(screen: ScreenView) {
        fragmentManager.apply {
            val currentFragment = findFragmentById(idFrameLayout)
            if (currentFragment == null || currentFragment.arguments?.getLong(ScreenViewImpl.SK_EXTRA_VIEW_KEY) != screen.key) {
                (screen as? ScreenViewImpl<out AppCompatActivity, out Fragment, out ViewBinding>)?.let { screenView ->
                    val trans = beginTransaction()
                    trans.customizeTransaction()
                    trans.replace(idFrameLayout, screenView.createFragmentWithKey())
                            .commit()
                }

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