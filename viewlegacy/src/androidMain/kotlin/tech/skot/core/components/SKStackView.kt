package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.SKTransitionAndroidLegacy


class SKStackView(
    proxy: SKStackViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val frameLayout: FrameLayout
) : SKComponentView<FrameLayout>(proxy, activity, fragment, frameLayout) {


    fun onLastScreen(lastScreen: Pair<SKScreenViewProxy<*>, SKTransitionAndroidLegacy?>?) {
        fragmentManager.apply {
            beginTransaction().apply {
                lastScreen?.second?.let {
                    setCustomAnimations(it.enterAnim, it.exitAnim)
                }
                if (lastScreen != null) {
                    replace(frameLayout.id, lastScreen.first.createFragment())
                } else {
                    findFragmentById(frameLayout.id)?.let { fragmentToRemove ->
                        remove(fragmentToRemove)
                    }
                }
                commit()
            }
        }
    }

}