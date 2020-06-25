package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.view.Action

abstract class StackViewImpl<A : AppCompatActivity, F : Fragment, B : ViewBinding> : ComponentViewImpl<A, F, B>(),
        StackView {

    data class PushScreen(val screen: ScreenView) : Action()
    object PopScreen : Action()
    object Clear:Action()

    abstract val idFrameLayout: Int

    open fun FragmentTransaction.customizeTransaction() {
    }

    private fun pushScreenNow(screen: ScreenView) {
        fragmentManager.apply {
//            SKLog.d("Il y a déjà ${backStackEntryCount} dans la backStack et on va en ajouter une")
           (screen as? ScreenViewImpl<out AppCompatActivity, out Fragment, out ViewBinding>)?.let { screenView ->
                val trans = beginTransaction()
                trans.customizeTransaction()
                trans.replace(idFrameLayout, screenView.createFragmentWithKey())
                        .addToBackStack(null)
                        .commit()
            }

        }
    }

    private fun popScreenNow() {
        fragmentManager.popBackStack()
    }

    private fun clearNow() {
        (1..fragmentManager.backStackEntryCount).forEach { _ ->
            fragmentManager.popBackStack()
        }
    }

    override fun pushScreen(screen: ScreenView) {
        messages.post(PushScreen(screen))
    }

    override fun popScreen() {
        messages.post(PopScreen)
    }

    override fun clear() {
        messages.post(Clear)
    }

    override fun treatAction(action: Action) {
        when (action) {
            is PushScreen -> pushScreenNow(action.screen)
            PopScreen -> popScreenNow()
            Clear -> clearNow()
            else -> super.treatAction(action)
        }

    }



}