package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class SKLoaderViewProxy:SKComponentViewProxy<View>(), SKLoaderVC {

    private val visibleLD = MutableSKLiveData<Boolean>(false)
    override var visible: Boolean by visibleLD


    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: View) =
            SKLoaderView(this, activity, fragment, binding).apply {
                visibleLD.observe {
                    onVisible(it)
                }
            }
}