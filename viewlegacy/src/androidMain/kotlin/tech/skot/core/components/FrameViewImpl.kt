package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog

class FrameViewImpl(activity: SKActivity, fragment: Fragment?, binding: FrameLayout, private val screens: Set<ScreenViewProxy<*>>) : ComponentViewImpl<FrameLayout>(activity, fragment, binding) {


    fun onScreen(screen: ScreenViewProxy<*>?) {
        if (screen != null && !screens.contains(screen)) {
            throw IllegalAccessException("The screen you try to add do this Frame is not part of the list, maybe you should use a Stack")
        }
        fragmentManager.apply {
            val trans = beginTransaction()

            if (screen != null) {
                val tag = screen.key.toString()
                val alreadyAddedFragment = findFragmentByTag(tag)
                if (alreadyAddedFragment != null) {
                    trans.show(alreadyAddedFragment)
                } else {
                    SKLog.d("---- adding ${screen::class.simpleName}")
                    trans.add(binding.id, screen.createFragment(), tag)
                }
                fragments.forEach {
                    if (it.tag != tag) {
                        trans.hide(it)
                    }
                }
            } else {
                fragments.forEach {
                    trans.hide(it)
                }
            }
            trans.commit()
        }
    }


}