package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.view.extensions.setVisible

class SKLoaderView(activity: SKActivity, fragment: Fragment?, binding: View):SKComponentView<View>(activity, fragment, binding) {

    fun onVisible(visible:Boolean) {
        binding.setVisible(visible)
    }

}