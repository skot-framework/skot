package tech.skot.components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import tech.skot.view.Action
import tech.skot.view.OpenScreen
import tech.skot.view.live.MutableSKLiveData
import kotlin.reflect.KClass

fun startView(onLink: ((encodedPath: String, encodedFragment: String?) -> Long?)? = null, getInitialView: () -> ScreenView) {
    ScreenViewImpl.getInitialViewImpl = { getInitialView() as ScreenViewImpl<*, *, *> }
    ScreenViewImpl.onLink = onLink
}

abstract class ScreenViewImpl<A : AppCompatActivity, F : Fragment, B : ViewBinding> : ComponentViewImpl<A, F, B>(),
        ScreenView {

    companion object {
        var counter: Long = 0
        val instances: MutableMap<Long, ScreenViewImpl<out AppCompatActivity, out Fragment, out ViewBinding>> = mutableMapOf()

        fun getInstance(key: Long): ScreenViewImpl<out AppCompatActivity, out Fragment, out ViewBinding>? {
            return (instances[key])
        }

        var getInitialViewImpl: (() -> ScreenViewImpl<*, *, *>)? = null
        var initialViewImplKey: Long? = null

        var onLink: ((encodedPath: String, encodedFragment: String?) -> Long?)? = null

        const val SK_EXTRA_VIEW_KEY = "SK_EXTRA_VIEW_KEY"
        const val SK_ARG_VIEW_KEY = "SK_ARG_VIEW_KEY"
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

    override fun openScreenWillFinish(screenToOpen: ScreenView) {
        messages.post(OpenScreen(screenToOpen))
    }

    override fun treatAction(action: Action) {
        when (action) {
            is OpenScreen -> openScreen(action.screen)
            else -> super.treatAction(action)
        }

    }

    @CallSuper
    override fun linkTo(lifecycleOwner: LifecycleOwner) {
        super.linkTo(lifecycleOwner)
        loadingLD.observe(lifecycleOwner) {
            onLoading(it)
        }
        onBackLD.observe(lifecycleOwner) {
            onOnBack(it)
        }
    }


    private val lifecycleOwner: LifecycleOwner
        get() = fragment ?: activity

    protected val context: Context
        get() = fragment?.context ?: activity

    var hasBeenInflated = false

    open fun inflate(layoutInflater: LayoutInflater,
                     activity: AppCompatActivity, fragment: Fragment?): View {
        initWith(activity as A, fragment as F?, inflateBinding(layoutInflater))
        hasBeenInflated = true
        linkTo(lifecycleOwner)
        return binding.root
    }

    abstract fun inflateBinding(layoutInflater: LayoutInflater): B


    abstract fun getActivityClass(): KClass<out AppCompatActivity>

    open val customTransitionAnimationIn: Pair<Int,Int>? = null
    open val customTransitionAnimationOut: Pair<Int,Int>? = null

    abstract fun createFragment(): Fragment

    fun createFragmentWithKey() = createFragment().apply {
        arguments = Bundle().apply {
            putLong(SK_ARG_VIEW_KEY, key)
        }
    }

    abstract fun onLoading(loading: Boolean)

    abstract fun onOnBack(onBack: (() -> Unit)?)

    protected open fun openScreen(screen: ScreenView) {
        getInstance(screen.key)?.let { screenToOpenImpl ->
            context.startActivity(
                    Intent(context, screenToOpenImpl.getActivityClass().java)
                            .apply {
                                putExtra(SK_EXTRA_VIEW_KEY, screen.key)
                            }
            )
            screenToOpenImpl.customTransitionAnimationIn?.let {
                activity.overridePendingTransition(it.first, it.second)
            }
        }

    }

    override fun onRemove() {
        super.onRemove()
        instances.remove(key)
        if (hasBeenInflated) {
            if (fragment == null) {
                activity.finish()
                customTransitionAnimationOut?.let {
                    activity.overridePendingTransition(it.first, it.second)
                }
            }
        }
    }
}