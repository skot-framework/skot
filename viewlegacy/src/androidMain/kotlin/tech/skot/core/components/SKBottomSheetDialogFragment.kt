package tech.skot.core.components

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.skot.viewlegacy.R

const val SK_BOTTOM_SHEET_DIALOG_EXPANDED = "SK_BOTTOM_SHEET_DIALOG_EXPANDED"
const val SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED = "SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED"
const val SK_BOTTOM_SHEET_DIALOG_FULL_HEIGHT = "SK_BOTTOM_SHEET_DIALOG_FULL_HEIGHT"
const val SK_BOTTOM_SHEET_DIALOG_RESIZE_ON_KEYBOARD = "SK_BOTTOM_SHEET_DIALOG_RESIZE_ON_KEYBOARD"

class SKBottomSheetDialogFragment() : BottomSheetDialogFragment() {

    private var screen: SKScreenView<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_RESIZE_ON_KEYBOARD, false) == true) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.sk_bottomsheet_dialog_resize)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .apply {
                if (arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_EXPANDED, true) != false) {
                    (this as? BottomSheetDialog)?.behavior?.state =
                        BottomSheetBehavior.STATE_EXPANDED
                }
                (this as? BottomSheetDialog)?.behavior?.skipCollapsed =
                    arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED, true) ?: true


                if (arguments?.getBoolean(SK_BOTTOM_SHEET_DIALOG_FULL_HEIGHT, true) == true) {
                    this.setOnShowListener {
                        val bottomSheetDialog = it as BottomSheetDialog
                        val parentLayout =
                            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                        parentLayout?.let { it ->
                            setupFullHeight(it)
                        }
                    }
                }




            }
    }


    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
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