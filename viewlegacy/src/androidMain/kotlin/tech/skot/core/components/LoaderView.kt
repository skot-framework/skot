package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.view.extensions.setVisible

class LoaderView(activity: SKActivity, fragment: Fragment?, binding: View):ComponentView<View>(activity, fragment, binding) {

    fun onVisible(visible:Boolean) {
        binding.setVisible(visible)
    }

}