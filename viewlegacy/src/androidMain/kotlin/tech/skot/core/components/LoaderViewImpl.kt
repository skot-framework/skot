package tech.skot.core.components

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import tech.skot.view.extensions.setVisible

class LoaderViewImpl(activity: SKActivity, fragment: Fragment?, binding: View):ComponentViewImpl<View>(activity, fragment, binding) {

    fun onVisible(visible:Boolean) {
        binding.setVisible(visible)
    }

}