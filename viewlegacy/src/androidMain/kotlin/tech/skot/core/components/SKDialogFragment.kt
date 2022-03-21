package tech.skot.core.components

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class SKDialogFragment : DialogFragment() {

    private var screen: SKScreenView<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = arguments?.getInt(
            ScreensManager.SK_ARGUMENT_DIALOG_STYLE,
            android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth
        ) ?: android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth
        setStyle(
            STYLE_NO_TITLE,
            style
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return arguments?.getLong(ScreensManager.SK_ARGUMENT_VIEW_KEY)?.let { viewKey ->
            ScreensManager.getInstance(viewKey)?.bindTo(activity as SKActivity, this, inflater)
                ?.apply {
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

    private var onDismiss: (() -> Unit)? = null

    fun setOnDismissListener(block: () -> Unit) {
        onDismiss = block
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }


}