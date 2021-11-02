package tech.skot.core.components

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog

class SKFrameView(
    proxy: SKFrameViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: FrameLayout,
    private val screens: Set<SKScreenViewProxy<*>>
) : SKComponentView<FrameLayout>(proxy, activity, fragment, binding) {


    init {
        fragmentManager.apply {
            val fragmentsToRemove = fragments.filter { it is SKFragment }
            if (fragmentsToRemove.isNotEmpty()) {
                beginTransaction().let { trans ->
                    fragmentsToRemove.forEach {
                        trans.remove(it)
                    }
                    trans.commitNowAllowingStateLoss()
                }
            }
        }
    }

    fun onScreen(screen: SKScreenViewProxy<*>?) {
        if (screen != null && !screens.contains(screen)) {
            throw IllegalAccessException("The screen you try to add do this Frame is not part of the list, maybe you should use a Stack")
        }
        fragmentManager.apply {
            val trans = beginTransaction()

            if (screen != null) {

                val tag = screen.key.toString()
                val alreadyAddedFragment = findFragmentByTag(tag)


                fragments.forEach {
                    if (it.tag != tag && !it.isHidden) {
//                        SKLog.d("###### ))) hide ${it::class.simpleName} ${(it as? SKFragment)?.screen?.let { it::class.simpleName }}  ${it.isHidden()}")
                        trans.hide(it)
                        it.onPauseRecursive()
                    }
                }

                if (alreadyAddedFragment != null) {
                    trans.show(alreadyAddedFragment)
                } else {
                    screen.createFragment().let { frag ->
                        trans.add(binding.id, frag, tag)
                    }

                }

                alreadyAddedFragment?.let {
                    trans.runOnCommit {
                        it.onResumeRecursive()
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