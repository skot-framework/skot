package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.view.Action
import kotlin.reflect.KClass

fun Fragment.onPauseRecursive() {
    onPause()
    childFragmentManager.fragments.forEach {
        it.onPauseRecursive()
    }
}
fun Fragment.onResumeRecursive() {
    onResume()
    childFragmentManager.fragments.forEach {
        it.onResumeRecursive()
    }
}



abstract class FrameKeepingScreensViewImpl<A : AppCompatActivity, F : Fragment, B : ViewBinding> : FrameViewImpl<A, F, B>() {


    private val mapViewClassTag = mutableMapOf<KClass<*>, String>()

    private var currentFragmentTag:String? = null
    override fun setScreenNow(screen: ScreenView) {
//        SKLog.d("FrameKeepingScreensViewImpl --- setScreenNow  ${screen::class} ${screen.key}")
        fragmentManager.apply {
            val currentFragment = currentFragmentTag?.let { findFragmentByTag(it) }//findFragmentById(idFrameLayout)
//            SKLog.d("FrameKeepingScreensViewImpl --- currentFragment key ${currentFragment?.getKey()}")
            if (currentFragment == null || currentFragment.getKey() != screen.key) {
                (screen as? ScreenViewImpl<out AppCompatActivity, out Fragment, out ViewBinding>)?.let { screenView ->

                    val trans = beginTransaction()
                    trans.customizeTransaction()


                    val tag = screen.key.toString()
                    val fragmentAlreadyAdded = fragmentManager.findFragmentByTag(tag)

                    currentFragmentTag = tag
                    if (fragmentAlreadyAdded != null) {
//                        SKLog.d("Will show fragmentAlreadyAdded tag  ${fragmentAlreadyAdded.tag}  key  ${fragmentAlreadyAdded.getKey()}")
                        fragmentManager.fragments.forEach {
//                            SKLog.d("fragment ${it.getKey()} ${it::class.simpleName}")
                            if (it != fragmentAlreadyAdded) {
//                                SKLog.d("fragment != alreadyAdded")
                                if (it.isVisible) {
//                                    SKLog.d("isVisible, will hide")
                                    trans.hide(it)
                                    it.onPauseRecursive()
                                }
                            }
                        }
                        trans.show(fragmentAlreadyAdded)
                        fragmentAlreadyAdded.onResumeRecursive()

                    } else {
//                        SKLog.d("Will Remove fragment form same view already Added if needed")
                        //Remove fragment form same view already Added if needed
                        val fragRemoved = mapViewClassTag[screenView::class]?.let {
                            fragmentManager.findFragmentByTag(it)?.also {
//                                SKLog.d("Will remove fragment tag  ${it.tag}  key  ${it.getKey()}")
                                trans.remove(it)
                            }
                        }
                        fragmentManager.fragments.forEach {
                            if (it != fragRemoved) {
//                                SKLog.d("Will hide fragment tag  ${it.tag}  key  ${it.getKey()}")
                                if (it.isVisible) {
                                    trans.hide(it)
                                    it.onPauseRecursive()
                                }
                            }
                        }
//                        SKLog.d("Will add created fragment tag  $tag")
                        trans.add(idFrameLayout, screenView.createFragmentWithKey(), tag)
                        mapViewClassTag[screenView::class] = tag
                    }
                    trans.commit()
                }

            }
        }
    }




}