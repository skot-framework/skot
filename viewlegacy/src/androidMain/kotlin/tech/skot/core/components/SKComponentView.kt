package tech.skot.core.components

import android.content.Context
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import tech.skot.core.view.Color
import tech.skot.view.live.SKLiveData
import tech.skot.view.live.SKMessage

abstract class SKComponentView<B : Any>(
    open val proxy: SKComponentViewProxy<B>,
    val activity: SKActivity,
    protected val fragment: Fragment?,
    val binding: B
) : LifecycleOwner {

    val context = fragment?.context ?: activity

    protected val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    override fun getLifecycle() = fragment?.viewLifecycleOwner?.lifecycle ?: activity.lifecycle

    var collectObservers = false

    private val componentObservers: MutableList<SKLiveData<*>.LifecycleOwnerObserver> by lazy {
        mutableListOf()
    }

    fun <D> SKLiveData<D>.observe(onChanged: (D) -> Unit) {
        observe(lifecycleOwner = this@SKComponentView, onChanged).let {
            if (collectObservers) {
                componentObservers.add(it)
            }
        }
    }

    @CallSuper
    open fun removeObservers() {
        componentObservers.forEach { it.remove() }
        componentObservers.clear()
    }

    fun <D> SKMessage<D>.observe(onReceive: (D) -> Unit) {
        observe(lifecycleOwner = this@SKComponentView, onReceive)
    }

    fun TextView.setTextColor(color: Color) {
        setTextColor(ContextCompat.getColor(context, color.res))
    }

    fun displayError(message: String) {
        displayError?.invoke(this, message)
            ?: throw IllegalAccessException("You have to define SKComponentView.displayError")
    }

    fun closeKeyboard() {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            activity.window.decorView.windowToken,
            0
        )
    }

    protected fun setOnWindowInset(
        onWindowInset: ((windowInsets: WindowInsets) -> Unit)?
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val loadedInsets = activity.window?.decorView?.rootWindowInsets
            if (loadedInsets != null) {
                onWindowInset?.invoke(loadedInsets)
            } else {
                ((binding as? ViewBinding)?.root ?: binding as? View)
                    ?.setOnApplyWindowInsetsListener { view, windowInsets ->
                        onWindowInset?.invoke(windowInsets)
                        windowInsets
                    }
            }
        } else {
//            TODO("VERSION.SDK_INT < M")
        }

    }

    companion object {
        var displayError: (SKComponentView<*>.(message: String) -> Unit)? = null
    }
}
