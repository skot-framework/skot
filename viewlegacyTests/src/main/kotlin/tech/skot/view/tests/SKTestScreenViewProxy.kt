package tech.skot.view.tests

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import tech.skot.core.components.*
import tech.skot.core.components.presented.SKBottomSheetViewProxy
import tech.skot.core.components.presented.SKDialogView
import tech.skot.core.components.presented.SKDialogViewProxy
import tech.skot.core.toColor
import tech.skot.core.view.Color
import tech.skot.view.tests.SKTestView.Companion.dummyVisiblityListener
import tech.skot.viewlegacytests.databinding.TestScreenBinding

class SKTestScreenViewProxy(
    content: List<SKComponentViewProxy<*>>,
    private val vertical: Boolean = true,
    private val color: Color? = null
) :
    SKScreenViewProxy<TestScreenBinding>() {
    override val visibilityListener = dummyVisiblityListener()

    private val box = SKBoxViewProxy(
        itemsInitial = content,
        hiddenInitial = false,
    )

    val dialog: SKDialogViewProxy = SKDialogViewProxy()
    val bottomSheet: SKBottomSheetViewProxy = SKBottomSheetViewProxy()

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TestScreenBinding,
    ): SKScreenView<TestScreenBinding> = SKTestScreenView(this, activity, fragment, binding).apply {
        dialog._bindTo(activity, fragment, Unit).also { subViews.add(it) }
        bottomSheet._bindTo(activity, fragment, Unit).also { subViews.add(it) }
        if (!vertical) {
            binding.box.orientation = LinearLayout.HORIZONTAL
        }

        color?.let { binding.box.setBackgroundColor(it.toColor(context)) }

        box._bindTo(activity, fragment, binding.box)
    }

    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): TestScreenBinding = TestScreenBinding.inflate(layoutInflater, parent, attachToParent)
}

class SKTestScreenView(
    override val proxy: SKTestScreenViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: TestScreenBinding
) : SKScreenView<TestScreenBinding>(proxy, activity, fragment, binding)