package tech.skot.core.components

import android.content.Context
import android.content.pm.PackageManager
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tech.skot.core.di.get
import tech.skot.core.view.SKPermission
import tech.skot.core.view.Style
import tech.skot.view.SKPermissionAndroid
import tech.skot.view.live.SKMessage
import java.util.concurrent.atomic.AtomicInteger


abstract class SKComponentViewProxy<B : Any> : SKComponentVC {

    protected val displayMessageMessage = SKMessage<SKComponentVC.Message>()

    override fun displayMessage(message: SKComponentVC.Message) {
        displayMessageMessage.post(message)
    }

    override fun displayErrorMessage(message: String) {
        displayMessage(SKComponentVC.Message.Error(message))
    }

    protected val closeKeyboardMessage = SKMessage<Unit>()

    override fun closeKeyboard() {
        closeKeyboardMessage.post(Unit)
    }

    data class PermissionsRequest(
        val requestCode: Int,
        val permissions: List<SKPermissionAndroid>,
        val onResult: (permissionsOk: List<SKPermissionAndroid>) -> Unit
    )


    protected val requestPermissionsMessage = SKMessage<PermissionsRequest>()
    override fun requestPermissions(
        permissions: List<SKPermission>,
        onResult: (permissionsOk: List<SKPermission>) -> Unit
    ) {
        requestPermissionsMessage.post(
            PermissionsRequest(
                requestCode = permissionsRequestCounter.getAndIncrement(),
                permissions = permissions as List<SKPermissionAndroid>
            ) { result ->
                onResult(result)
            })
    }


    override fun hasPermission(vararg permission: SKPermission): Boolean {
        val context: Context = get()
        return permission.all {
            ContextCompat.checkSelfPermission(
                context,
                (it as SKPermissionAndroid).name
            ) == PackageManager.PERMISSION_GRANTED
        }

    }


    override var style: Style? = null

    abstract fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: B
    ): SKComponentView<B>

    fun _bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: B
    ) =
        bindTo(activity, fragment, binding).apply {
            displayMessageMessage.observe {
                displayMessage(it)
            }
            closeKeyboardMessage.observe {
                closeKeyboard()
            }
            requestPermissionsMessage.observe {
                requestPermissions(it)
            }
        }

    open fun saveState() {
        //surchargée quand le component a un état à sauver
    }

    @CallSuper
    override fun onRemove() {

    }

    open val layoutId: Int? = null

    open fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): B {
        return layoutId?.let {
            val b = layoutInflater.inflate(it, parent, false)
            if (attachToParent) {
                parent?.addView(b)
            }
            b.tag = this.hashCode()
            b as B
        } ?: throw Exception("Vous devez implémenter layoutId ou bien la méthode inflate pour le composant ${this::class.simpleName}")
    }

    fun bindToView(
        activity: SKActivity,
        fragment: Fragment?,
        view: View
    ) =
        _bindTo(activity, fragment, bindingOf(view))

    fun inflateInParentAndBind(activity: SKActivity, fragment: Fragment?, parent: ViewGroup):SKComponentView<*> {
        val inflater = (fragment?.layoutInflater ?: activity.layoutInflater).let { layoutInflater ->
            parent.context?.let { parentContext ->
                layoutInflater.cloneInContext(
                    style?.let { theme ->
                        ContextThemeWrapper(parentContext, theme.res)
                    } ?: parentContext
                )
            } ?: layoutInflater
        }
        return _bindTo(activity, fragment, inflate(inflater, parent, true))
    }

    fun inflateAndBind(activity: SKActivity, fragment: Fragment?) : B {
        val inflater = (fragment?.layoutInflater ?: activity.layoutInflater)
        val inflated = inflate(inflater, null, false)
        _bindTo(activity, fragment, inflated)
        return inflated
    }

    open fun bindingOf(view: View): B {
        return (view as? B)
            ?: throw IllegalStateException("You cant't bind this component to a view")
    }

    fun bindToItemView(activity: SKActivity, fragment: Fragment?, view: View): SKComponentView<B> {
        if (layoutId == null) {
            throw IllegalStateException("You cant't bind this component to an Item's view, it has no layout Id")
        } else {
            return _bindTo(activity, fragment, bindingOf(view))
        }
    }

    companion object {
        val permissionsRequestCounter = AtomicInteger(0)
    }
}

