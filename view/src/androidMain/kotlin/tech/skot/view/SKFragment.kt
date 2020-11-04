package tech.skot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.skot.components.ScreenViewImpl

fun Fragment.onCreateViewSK(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?
):Pair<ScreenViewImpl<*,*,*>, View>? {
    return arguments?.getLong(ScreenViewImpl.SK_ARG_VIEW_KEY)?.let { viewKey ->
        val screenViewImpl = ScreenViewImpl.getInstance(viewKey)
        if (screenViewImpl != null) {
            return Pair(screenViewImpl, screenViewImpl.inflate(inflater, activity as AppCompatActivity, this@onCreateViewSK))
        }
        else {
            return null
        }

    }
}



abstract class SKFragment : Fragment() {

    private var screenViewImpl:ScreenViewImpl<*,*,*>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return onCreateViewSK(inflater, container, savedInstanceState)?.let {(screenViewImpl, view)->
            this.screenViewImpl = screenViewImpl
            view
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenViewImpl?.cleanViewReferences()
    }
}

abstract class SKBottomSheetDialogFragment: BottomSheetDialogFragment() {

    private var screenViewImpl:ScreenViewImpl<*,*,*>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return onCreateViewSK(inflater, container, savedInstanceState)?.let {(screenViewImpl, view)->
            this.screenViewImpl = screenViewImpl
            view
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenViewImpl?.cleanViewReferences()
    }

}