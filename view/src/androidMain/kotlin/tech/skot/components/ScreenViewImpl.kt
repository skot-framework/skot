package tech.skot.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import tech.skot.components.ScreenViewImpl.Companion.SK_ARG_VIEW_KEY
import tech.skot.view.*
import tech.skot.view.live.MutableSKLiveData
import kotlin.reflect.KClass

fun startView(onLink: ((encodedPath: String, encodedFragment: String?) -> Long?)? = null, getInitialView: () -> ScreenView) {
    ScreenViewImpl.getInitialViewImpl = { getInitialView() as ScreenViewImpl<*, *, *> }
    ScreenViewImpl.onLink = onLink
}

fun Fragment.getKey():Long? = arguments?.getLong(SK_ARG_VIEW_KEY)

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


        fun openScreen(context:Context, activity:Activity, screen: ScreenView) {
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

    private val onBackLD = MutableSKLiveData<(() -> Unit)?>(null)
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
            is ShowBottomSheetDialog -> showBottomSheetDialogNow(action.screen)
            Dismiss -> dismissNow()
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


    protected val context: Context
        get() = fragment?.context ?: activity

    var hasBeenInflated = false

    open fun inflate(layoutInflater: LayoutInflater,
                     activity: AppCompatActivity, fragment: Fragment?): View {
        initWith(activity as A, fragment as F?, inflateBinding(layoutInflater))
        hasBeenInflated = true
        linkTo(lifeCycleOwner)
        return binding.root
    }

    abstract fun inflateBinding(layoutInflater: LayoutInflater): B


    abstract fun getActivityClass(): KClass<out AppCompatActivity>

    open val customTransitionAnimationIn: Pair<Int,Int>? = null
    open val customTransitionAnimationOut: Pair<Int,Int>? = null

    protected abstract fun createFragment(): Fragment

    fun createFragmentWithKey() = createFragment().apply {
        arguments = Bundle().apply {
            putLong(SK_ARG_VIEW_KEY, key)
        }
    }

    abstract fun onLoading(loading: Boolean)

    abstract fun onOnBack(onBack: (() -> Unit)?)

    protected open fun openScreen(screen: ScreenView) {
        Companion.openScreen(context, activity, screen)
    }

    override fun showBottomSheetDialog(screen: ScreenView) {
        messages.post(ShowBottomSheetDialog(screen))
    }

    protected open fun getBottomSheetStyle():Int = 0

    protected var bottomSheetDialog:BottomSheetDialog? = null

    protected fun showBottomSheetDialogNow(screen: ScreenView) {
        getInstance(screen.key)?.let { screenToOpenImpl ->

            BottomSheetDialog(context, getBottomSheetStyle()).apply {
                screenToOpenImpl.bottomSheetDialog = this
                setContentView(screenToOpenImpl.inflate(layoutInflater, activity, fragment))
                show()
            }
        }
    }

    override fun dismiss() {
        messages.post(Dismiss)
    }

    private fun dismissNow() {
        bottomSheetDialog?.dismiss()
        bottomSheetDialog = null
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