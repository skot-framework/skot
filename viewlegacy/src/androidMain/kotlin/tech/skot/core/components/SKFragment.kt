package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog

fun Fragment.onPauseRecursive() {
    (this as? SKFragment)?.screen?.onPause()
    childFragmentManager.fragments.forEach {
        if (it.isResumed) {
            it.onPauseRecursive()
        }
    }
}
fun Fragment.onResumeRecursive() {
    (this as? SKFragment)?.screen?.onResume()
    childFragmentManager.fragments.forEach {
        if (it.isResumed) {
            it.onResumeRecursive()
        }
    }
}

fun Fragment.isHiddenRecursive():Boolean = this.isHidden || parentFragment?.isHiddenRecursive() == true


open class SKFragment : Fragment() {

    private var screenKey: Long? = null
    var screen:SKScreenView<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return arguments?.getLong(ScreensManager.SK_ARGUMENT_VIEW_KEY)?.let { viewKey ->
            ScreensManager.getInstance(viewKey)?.bindTo(activity as SKActivity, this, inflater)
                    ?.also {
                        screenKey = viewKey
                        screen = it
                    }?.view
                    ?: View(context).also { screenKey == null }

        }
    }

    override fun onPause() {
        super.onPause()
        if (isAdded && !isHiddenRecursive()) {
            screen?.onPause()
        }
//        else {
//            SKLog.d("###### - pas de onPause ${screen?.let { it::class.simpleName }}")
//        }

    }

    override fun onResume() {
        super.onResume()
//        SKLog.d("SKFragment ----> ${screen?.let { it::class.simpleName }} isAdded=${isAdded} isDetached=${isDetached} isHidden=${isHidden} isHiddenRecursive=${isHiddenRecursive()}")
        if (isAdded && !isHiddenRecursive()){
            screen?.onResume()
        }
//        else {
//            SKLog.d("###### - pas de onResume ${screen?.let { it::class.simpleName }}")
//        }
    }

//    override fun onDestroy() {
//        SKLog.d("SKFragment ${hashCode()} onDestroy ${screenKey}")
//        super.onDestroy()
//    }
//
//    override fun onDestroyView() {
//        SKLog.d("SKFragment ${hashCode()} onDestroyView ${screenKey}")
//        super.onDestroyView()
//    }
}