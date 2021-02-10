package tech.skot.core.components

import androidx.fragment.app.Fragment


class StackViewImpl(activity: SKActivity, fragment: Fragment?, private val frameLayoutId: Int) : ComponentViewImpl<Int>(activity, fragment, frameLayoutId) {

    fun onScreens(screens: List<ScreenViewProxy<*>>) {

        val lastScreen = screens.lastOrNull()
        if (lastScreen != null) {

            fragmentManager.apply {
                val trans = beginTransaction()
                trans.replace(frameLayoutId, lastScreen.createFragment())
                trans.commit()
            }
        } else {
        }

    }

}