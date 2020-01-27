package tech.skot.components

import android.view.View
import tech.skot.view.Container
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment
import tech.skot.view.extensions.setVisible
import kotlin.reflect.KClass

abstract class ScreenObserver<A : SKActivity, F : SKFragment>(
        container: Container<out A, out F>,
        private val loadingView: View?
) : ComponentObserver<A, F>(container), ScreenObserverInterface {

    override fun onLoading(state: Boolean) {
        loadingView?.setVisible(state)
    }

    override fun onOnBack(state: (() -> Unit)?) {
        activity.onBackPressedAction = state
    }

    override fun openScreen(screen: ScreenView) {
        context.startActivity(SKActivity.getIntent(getActivityClass(), context, screen.key))
    }

    abstract fun getActivityClass(): KClass<out SKActivity>

    override fun onRemove() {
        super.onRemove()
        if (fragment == null) {
            activity.activity.finish()
        }

    }
}