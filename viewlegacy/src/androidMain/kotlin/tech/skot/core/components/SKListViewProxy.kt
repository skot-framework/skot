package tech.skot.core.components

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R

class SKListViewProxy(private val vertical:Boolean, private val reverse:Boolean, private val nbColumns:Int?, private val animate:Boolean, private val animateItem:Boolean) : SKComponentViewProxy<RecyclerView>(), SKListVC {

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
            SKListView(vertical, reverse, nbColumns, animate, animateItem, this, activity, fragment, binding).apply {
                itemsLD.observe {
                    onItems(it)
                }
                saveSignal.observe {
//                    SKLog.d("SKListViewProxy receive Save Signal")
                    _state = saveState()
                }
                srollToPositionMessage.observe {
                    scrollToPosition(it)
                }
                _state?.let {
//                    SKLog.d("SKListViewProxy restoreState")
                    restoreState(it)
                }
            }




}