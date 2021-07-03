package tech.skot.core.components

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData
import tech.skot.viewlegacy.R

class SKBoxViewProxy(itemsInitial: List<SKComponentViewProxy<*>>, hiddenInitial:Boolean?) :
    SKComponentViewProxy<ViewGroup>(), SKBoxVC {

    private val itemsLD: MutableSKLiveData<List<SKComponentViewProxy<*>>> =
        MutableSKLiveData(itemsInitial)

    override var items: List<SKComponentVC>
        get() = itemsLD.value
        set(newVal) {
            itemsLD.postValue(newVal as List<SKComponentViewProxy<*>>)
        }

    override val layoutId = R.layout.sk_box_horizontal

    private val hiddenLD: MutableSKLiveData<Boolean?> = MutableSKLiveData(hiddenInitial)

    override var hidden: Boolean? by hiddenLD

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: ViewGroup,
        collectingObservers: Boolean
    ): SKComponentView<ViewGroup> =
        SKBoxView(activity, fragment, binding).apply {
            collectObservers = collectingObservers
            itemsLD.observe {
                onItems(it)
            }
            hiddenLD.observe {
                onHidden(it)
            }
        }

}