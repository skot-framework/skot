package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog

open class SKFragment : Fragment() {

    private var screenKey: Long? = null
    var screen: SKScreenView<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return arguments?.getLong(ScreensManager.SK_ARGUMENT_VIEW_KEY)?.let { viewKey ->
            ScreensManager.getInstance(viewKey)?.bindTo(activity as SKActivity, this, inflater)
                ?.also {
                    screenKey = viewKey
                    screen = it
                }?.view
                ?: View(context).also { screenKey = null }

        }
    }

    override fun onPause() {
        if (isAdded || isRemoving) {
            screen?.onPause()
        }
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            screen?.onResume()
        }
    }

}