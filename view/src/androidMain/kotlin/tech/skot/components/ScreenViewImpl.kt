package tech.skot.components

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import tech.skot.contract.components.ScreenObserverInterface
import tech.skot.contract.components.ScreenView
import tech.skot.view.*
import tech.skot.view.live.MutableSKLiveData


abstract class ScreenViewImpl<O : ScreenObserverInterface, A : SKActivity, F : SKFragment> : ComponentViewImpl<O, A, F>(),
        ScreenView {

    companion object {
        var counter: Long = 0
        val instances: MutableMap<Long, ScreenViewImpl<*, *, *>> = mutableMapOf()

        fun <S> getInstance(key: Long): S {
            return (instances[key] as S?)!!
        }
    }

    override val key = counter++


    init {
        instances[key] = this
    }

    private val loadingLD = MutableSKLiveData<Boolean>(false)
    override var loading: Boolean
        get() = loadingLD.value
        set(newVal) {
            loadingLD.postValue(newVal)
        }

    val onBackLD = MutableSKLiveData<(() -> Unit)?>(null)
    override var onBack: (() -> Unit)?
        get() = onBackLD.value
        set(newVal) {
            onBackLD.postValue(newVal)
        }


    override var onTop: ScreenView? = null
        set(newVal) {
            field = newVal
            newVal?.let { messages.post(OpenScreen(newVal)) }
        }

    override fun treatAction(action: Action, observer: O) {
        when (action) {
            is OpenScreen -> observer.openScreen(action.screen)
            else -> super.treatAction(action, observer)
        }

    }

    override fun linkTo(observer: O, lifecycleOwner: LifecycleOwner) {

        super.linkTo(observer, lifecycleOwner)

        loadingLD.observe(lifecycleOwner) {
            observer.onLoading(it)
        }

        onBackLD.observe(lifecycleOwner) {
            observer.onOnBack(it)
        }
    }

    abstract fun inflate(layoutInflater: LayoutInflater,
                         container: Container<out A, out F>): View
}