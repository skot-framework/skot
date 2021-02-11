package tech.skot.core.components

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tech.skot.view.live.MutableSKLiveData

class SKListViewProxy() : ComponentViewProxy<RecyclerView>(), SKListVC {

    private val itemsLD: MutableSKLiveData<List<ComponentViewProxy<*>>> = MutableSKLiveData(emptyList())

    override var items: List<ComponentVC>
        get() = itemsLD.value
        set(newVal) {
            val newProxyItems = newVal as List<ComponentViewProxy<*>>
//            itemsLD.value.lastOrNull()?.let {
//                if (newProxyList.lastOrNull() != it && newProxyList.contains(it)) {
//                    it.saveState()
//                }
//            }
            itemsLD.postValue(newProxyItems)
        }


    override fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, binding: RecyclerView) =
            SKListViewImpl(activity, fragment, binding).apply {
                itemsLD.observe {
                    onItems(it)
                }
            }




}