package tech.skot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class SKFragment : Fragment() {

    companion object {
        const val ARG_VIEW_KEY = "ARG_VIEW_KEY"

        inline fun <reified F : SKFragment> getInstance(viewKey: Long) =
                F::class.java.newInstance().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_VIEW_KEY, viewKey)
                    }
                }
    }


    abstract fun getScreenViewForKey(key: Long, inflater: LayoutInflater): View?

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return (activity as? SKActivity)?.let { _ ->
            val viewKey = arguments?.getLong(ARG_VIEW_KEY)
            getScreenViewForKey(viewKey ?: throw Exception("BaseFragment sans viewKey"), inflater)
        }
    }


}