package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog

open class SKFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return arguments?.getLong(ScreensManager.SK_ARGUMENT_VIEW_KEY)?.let { viewKey ->
            ScreensManager.getInstance(viewKey)?.bindTo(activity as SKActivity, this, inflater)
        }
    }


//    override fun onDestroy() {
//        SKLog.d("SKFragment ${hashCode()} onDestroy")
//        super.onDestroy()
//    }
//
//    override fun onDestroyView() {
//        SKLog.d("SKFragment ${hashCode()} onDestroyView")
//        super.onDestroyView()
//    }
}