package tech.skot.core.components

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.core.toColor
import tech.skot.core.view.Color
import tech.skot.view.SKPermissionAndroid
import tech.skot.view.live.SKLifecycle
import tech.skot.view.live.SKLifecycleOwner
import tech.skot.view.live.SKLiveData
import tech.skot.view.live.SKMessage

abstract class SKComponentView<B : Any>(
    open val proxy: SKComponentViewProxy<B>,
    val activity: SKActivity,
    protected val fragment: Fragment?,
    val binding: B
)  {

    val context = fragment?.context ?: activity

    protected val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    private val baseLifecycle = fragment?.viewLifecycleOwner?.lifecycle ?: activity.lifecycle

    var lifecycleOwner: SKLifecycleOwner = SKLifecycleOwner(SKLifecycle(baseLifecycle))

    val subViews: MutableList<SKComponentView<*>> by lazy {
        mutableListOf()
    }

    fun <D> SKLiveData<D>.observe(onChanged: (D) -> Unit) {
        setObserver(lifecycleOwner = lifecycleOwner, onChanged)
    }

    @CallSuper
    open fun onRecycle() {

        lifecycleOwner.lifecycle.recycled = true
        lifecycleOwner = SKLifecycleOwner(SKLifecycle(baseLifecycle))
        subViews.forEach { it.onRecycle() }
        subViews.clear()

    }


    fun <D> SKMessage<D>.observe(onReceive: (D) -> Unit) {
        setObserver(lifecycleOwner = lifecycleOwner, onReceive)
    }

    fun TextView.setTextColor(color: Color) {
        setTextColor(color.toColor(context))
    }

    fun displayMessage(message: SKComponentVC.Message) {
        displayMessage?.invoke(this, message)
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


    private fun SKPermissionAndroid.isGranted(): Boolean =
        ContextCompat.checkSelfPermission(context, name) == PackageManager.PERMISSION_GRANTED


    var pendingPermissionsRequests: MutableList<SKComponentViewProxy.PermissionsRequest>? = null

    fun requestPermissions(permissionsRequest: SKComponentViewProxy.PermissionsRequest) {
        when {
            (permissionsRequest.permissions.all { it.isGranted() }) -> {
                permissionsRequest.onResult(permissionsRequest.permissions)
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPendingPermissionsRequest(permissionsRequest)
                    activity.requestPermissions(
                        permissionsRequest.permissions.map { it.name }.toTypedArray(),
                        permissionsRequest.requestCode
                    )
                } else {
                    SKLog.e(
                        Exception("You need to declare some permissions in your manifest"),
                        permissionsRequest.permissions.filter { !it.isGranted() }.joinToString()
                    )
                    permissionsRequest.onResult(
                        permissionsRequest.permissions.filter { it.isGranted() }
                    )
                }
            }
        }
    }

    private fun addPendingPermissionsRequest(permissionsRequest: SKComponentViewProxy.PermissionsRequest) {
        val currentPendingPermissionsRequests = pendingPermissionsRequests
        if (currentPendingPermissionsRequests == null) {
            pendingPermissionsRequests = mutableListOf(permissionsRequest)
            ScreensManager.permissionsResults.observe { result ->
                pendingPermissionsRequests?.let { requests ->
                    requests.find { it.requestCode == result.requestCode }
                        ?.let { request ->
                            request.onResult(result.grantedPermissions)
                            requests.remove(request)
                        }
                }
            }
        } else {
            currentPendingPermissionsRequests.add(permissionsRequest)
        }
    }


    companion object {
        var displayMessage: (SKComponentView<*>.(message: SKComponentVC.Message) -> Unit)? = null
    }
}