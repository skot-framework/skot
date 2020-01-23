package tech.skot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.components.ScreenViewImpl
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface SKFragment {

    companion object {
        const val ARG_VIEW_KEY = "ARG_VIEW_KEY"

        fun getInstance(viewKey: Long, fragmentClass: KClass<out SKFragment>) = fragmentClass.createInstance().apply {
            fragment.arguments = Bundle().apply {
                putLong(ARG_VIEW_KEY, viewKey)
            }
        }

    }

    fun getScreenViewForKey(key: Long, inflater: LayoutInflater): View?

    val fragment: Fragment

    fun onCreateViewSK(inflater: LayoutInflater,
                       container: ViewGroup?,
                       savedInstanceState: Bundle?
    ) = (fragment.activity as? SKActivity)?.let { _ ->
        val viewKey = fragment.arguments?.getLong(ARG_VIEW_KEY)
        getScreenViewForKey(viewKey ?: throw Exception("BaseFragment sans viewKey"), inflater)
    }

}

abstract class SKFragmentImpl : Fragment(), SKFragment {

    override fun getScreenViewForKey(key: Long, inflater: LayoutInflater) = (activity as? SKActivityImpl)?.let {
        ScreenViewImpl.getInstance<ScreenViewImpl<*, SKActivity, SKFragment>>(key)
                .inflate(layoutInflater, Container(it, this))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return onCreateViewSK(inflater, container, savedInstanceState)
    }

    override val fragment = this
}