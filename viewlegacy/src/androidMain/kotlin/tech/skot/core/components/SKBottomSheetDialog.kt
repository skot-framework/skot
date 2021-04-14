package tech.skot.core.components

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//class SKBottomSheetDialog(context: Context, theme:Int): BottomSheetDialog(context, theme), LifecycleOwner {
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        _lifecycle.currentState = Lifecycle.State.DESTROYED
//    }
//
//    override fun onStart() {
//        super.onStart()
//        _lifecycle.currentState = Lifecycle.State.STARTED
//    }
//
//    private val _lifecycle= LifecycleRegistry(this)
//
//    override fun getLifecycle(): Lifecycle {
//        return _lifecycle
//    }
//}

class SKBottomSheetDialogFragment(): BottomSheetDialogFragment() {

    private var screen:ScreenView<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return arguments?.getLong(ScreensManager.SK_ARGUMENT_VIEW_KEY)?.let { viewKey ->
            ScreensManager.getInstance(viewKey)?.bindTo(activity as SKActivity, this, inflater)?.apply {
                screen = this
            }?.view
        }
    }

    override fun onResume() {
        super.onResume()
        screen?.onResume()
    }

    override fun onPause() {
        super.onPause()
        screen?.onPause()
    }

    private var onDismiss:(()->Unit)? = null

    fun setOnDismissListener(block:()->Unit) {
        onDismiss = block
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }


}