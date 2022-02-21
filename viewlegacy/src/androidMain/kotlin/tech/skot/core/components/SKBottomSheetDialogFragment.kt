package tech.skot.core.components

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val SK_BOTTOM_SHEET_DIALOG_EXPANDED = "SK_BOTTOM_SHEET_DIALOG_EXPANDED"
const val SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED = "SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED"
const val SK_BOTTOM_SHEET_DIALOG_HIDEABLE = "SK_BOTTOM_SHEET_DIALOG_HIDEABLE"
const val SK_BOTTOM_SHEET_DIALOG_PEEK_HEIGHT = "SK_BOTTOM_SHEET_DIALOG_PEEK_HEIGHT"

class SKBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private var screen: SKScreenView<*>? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .apply {
                if (arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_EXPANDED, true) != false) {
                    (this as? BottomSheetDialog)?.behavior?.state =
                        BottomSheetBehavior.STATE_EXPANDED
                }
                (this as? BottomSheetDialog)?.behavior?.skipCollapsed =
                    arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED, true) ?: true

                // height of the collapsed size
                val height = arguments?.getInt(SK_BOTTOM_SHEET_DIALOG_PEEK_HEIGHT, BottomSheetBehavior.PEEK_HEIGHT_AUTO ) ?: BottomSheetBehavior.PEEK_HEIGHT_AUTO
                (this as? BottomSheetDialog)?.behavior?.peekHeight = height

                // disable  BottomSheetBehavior.STATE_HIDDEN
                val hideable =  arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_HIDEABLE, true) ?: true
                (this as? BottomSheetDialog)?.behavior?.isHideable = hideable


                if(!hideable) {
                    window?.setDimAmount(0f)
                }
                setCancelable(hideable)
                setCanceledOnTouchOutside(hideable)

            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as? View?)?.apply {
            backgroundTintMode = PorterDuff.Mode.CLEAR
            backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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