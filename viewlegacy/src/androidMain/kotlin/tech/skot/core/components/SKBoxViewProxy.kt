package tech.skot.core.components

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R

class SKBoxViewProxy(
    itemsInitial: List<SKComponentViewProxy<*>>, hiddenInitial: Boolean? = false,
    override val asItemVertical: Boolean? = null
) :
    SKComponentViewProxy<ViewGroup>(), SKBoxVC {

    private val itemsLD: MutableSKLiveData<List<SKComponentViewProxy<*>>> =
        MutableSKLiveData(itemsInitial)

    override var items: List<SKComponentVC>
        get() = itemsLD.value
        set(newVal) {
            itemsLD.postValue(newVal as List<SKComponentViewProxy<*>>)
        }

    override val layoutId =
        if (asItemVertical == true) R.layout.sk_box_vertical else R.layout.sk_box_horizontal

    private val hiddenLD: MutableSKLiveData<Boolean?> = MutableSKLiveData(hiddenInitial)

    override var hidden: Boolean? by hiddenLD

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: ViewGroup
    ): SKComponentView<ViewGroup> =
        SKBoxView(this, activity, fragment, binding).apply {
            itemsLD.observe {
                onItems(it)
            }
            hiddenLD.observe {
                onHidden(it)
            }
        }

}