package tech.skot.core.components

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R

open class SKListViewProxy(
    private val layoutMode: SKListVC.LayoutMode = SKListVC.LayoutMode.Linear(true),
    private val reverse: Boolean = false,
    private val animate: Boolean = true,
    private val animateItem: Boolean = false
) : SKComponentViewProxy<RecyclerView>(), SKListVC {

    private val itemsLD: MutableSKLiveData<List<Triple<SKComponentViewProxy<*>, Any, (()->Unit)?>>> = MutableSKLiveData(emptyList())

    override var items: List<Triple<SKComponentVC, Any, (()->Unit)?>>
        get() = itemsLD.value
        set(newVal) {
            itemsLD.postValue(newVal as List<Triple<SKComponentViewProxy<*>, Any, (()->Unit)?>>)
        }

    private val srollToPositionMessage = SKMessage<Int>()
    override fun scrollToPosition(position: Int) {
        srollToPositionMessage.post(position)
    }

    private val saveSignal: SKMessage<Unit> = SKMessage()
    private var _state: Parcelable? = null

    override fun saveState() {
        saveSignal.post(Unit)
    }

    override val layoutId = R.layout.sk_list

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: RecyclerView) =
            SKListView(layoutMode, reverse, animate, animateItem, this, activity, fragment, binding).apply {
                itemsLD.observe {
                    onItems(it)
                }
                saveSignal.observe {
                    _state = saveState()
                }
                srollToPositionMessage.observe {
                    scrollToPosition(it)
                }
                _state?.let {
                    restoreState(it)
                }
            }




}