package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog


class SKStackView(proxy:SKStackViewProxy, activity: SKActivity, fragment: Fragment?, private val frameLayout: FrameLayout) : SKComponentView<FrameLayout>(proxy, activity, fragment, frameLayout) {


    fun onState(state:StateProxy) {
        val lastScreen = state.screens.lastOrNull()
        if (lastScreen != null) {
            fragmentManager.apply {
                val oldFrag = findFragmentById(frameLayout.id)
                beginTransaction().apply {
                    replace(frameLayout.id, lastScreen.createFragment())
                    commit()
                }
                oldFrag?.onPauseRecursive()
            }
        }
    }

}