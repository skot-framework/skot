package tech.skot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import tech.skot.components.ScreenViewImpl

fun Fragment.onCreateViewSK(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?
) =
        arguments?.getLong(ScreenViewImpl.SK_ARG_VIEW_KEY)?.let { viewKey ->
            ScreenViewImpl.getInstance(viewKey)?.inflate(inflater, activity as AppCompatActivity, this)
        }


abstract class SKFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return onCreateViewSK(inflater, container, savedInstanceState)
    }

}