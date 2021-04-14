package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment


class StackView(activity: SKActivity, fragment: Fragment?, private val frameLayout: FrameLayout) : ComponentView<FrameLayout>(activity, fragment, frameLayout) {

    fun onScreens(screens: List<ScreenViewProxy<*>>) {

        val lastScreen = screens.lastOrNull()
        if (lastScreen != null) {

            fragmentManager.apply {
                val trans = beginTransaction()
                trans.replace(frameLayout.id, lastScreen.createFragment())
                trans.commit()
            }
        } else {
        }

    }

}