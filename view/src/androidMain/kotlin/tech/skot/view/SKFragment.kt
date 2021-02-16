package tech.skot.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.skot.components.ScreenViewImpl
import tech.skot.core.SKLog

fun Fragment.onCreateViewSK(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?
): Pair<ScreenViewImpl<*, *, *>, View>? {
    return arguments?.getLong(ScreenViewImpl.SK_ARG_VIEW_KEY)?.let { viewKey ->
        val screenViewImpl = ScreenViewImpl.getInstance(viewKey)
        if (screenViewImpl != null) {
            return Pair(screenViewImpl, screenViewImpl.inflate(inflater, activity as AppCompatActivity, this@onCreateViewSK))
        } else {
            if (ScreenViewImpl.counter > 1L) {
                SKLog.e("Fragment.onCreateViewSK -> No View for key $viewKey", NullPointerException("screenViewImpl instance not found"))
            }
            return null
        }

    }
}


abstract class SKFragment : Fragment() {

    private var screenViewImpl: ScreenViewImpl<*, *, *>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return onCreateViewSK(inflater, container, savedInstanceState)?.let { (screenViewImpl, view) ->
            this.screenViewImpl = screenViewImpl
            view
        } ?: View(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenViewImpl?.cleanViewReferences()
    }

}

//class SKBottomSheetDialogFragment : BottomSheetDialogFragment() {
//
//    private var screenViewImpl: ScreenViewImpl<*, *, *>? = null
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        SKLog.d("---- onCreateView SKBottomSheetDialogFragment ${this::class.simpleName}")
//        return onCreateViewSK(inflater, container, savedInstanceState)?.let { (screenViewImpl, view) ->
//            this.screenViewImpl = screenViewImpl
//            view
//        } ?: View(context)
//    }
//
//    override fun onDestroyView() {
//        SKLog.d("---- onDestroyView SKBottomSheetDialogFragment ${this::class.simpleName}")
//        super.onDestroyView()
//        screenViewImpl?.cleanViewReferences()
//    }
//
//}

class SKBottomSheetDialog(val screenViewImpl: ScreenViewImpl<*, *, *>, context:Context, theme:Int): BottomSheetDialog(context, theme), LifecycleOwner {



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _lifecycle.currentState = Lifecycle.State.DESTROYED
        screenViewImpl.cleanViewReferences()
    }

    override fun onStart() {
        super.onStart()
        _lifecycle.currentState = Lifecycle.State.STARTED
    }

   private val _lifecycle= LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return _lifecycle
    }
}