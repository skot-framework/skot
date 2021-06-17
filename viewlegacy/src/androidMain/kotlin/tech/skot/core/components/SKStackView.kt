package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment


class SKStackView(activity: SKActivity, fragment: Fragment?, private val frameLayout: FrameLayout) : SKComponentView<FrameLayout>(activity, fragment, frameLayout) {


    fun onState(state:StateProxy) {
        val lastScreen = state.screens.lastOrNull()
        if (lastScreen != null) {
            fragmentManager.apply {
                val trans = beginTransaction()
                trans.replace(frameLayout.id, lastScreen.createFragment())
                trans.commit()
            }
        }
    }

}