package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment


class SKStackView(
    proxy: SKStackViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val frameLayout: FrameLayout
) : SKComponentView<FrameLayout>(proxy, activity, fragment, frameLayout) {


    fun onLastScreen(lastScreen: SKScreenViewProxy<*>?) {
        fragmentManager.apply {
            beginTransaction().apply {
                if (lastScreen != null) {
                    replace(frameLayout.id, lastScreen.createFragment())
                } else {
                    findFragmentById(frameLayout.id)?.let { fragmentToRemove ->
                        remove(fragmentToRemove)
                    }
                }
                commit()
            }
        }
    }

    fun onState(state: StateProxy) {
        val lastScreen = state.screens.lastOrNull()
        if (lastScreen != null) {
            fragmentManager.apply {
                beginTransaction().apply {
                    replace(frameLayout.id, lastScreen.createFragment())
                    commit()
                }
            }
        }
    }

}