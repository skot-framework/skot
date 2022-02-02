package tech.skot.view.tests

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import tech.skot.core.components.*
import tech.skot.view.tests.SKTestView.Companion.dummyVisiblityListener
import tech.skot.viewlegacytests.databinding.TestScreenBinding

class SKTestScreenViewProxy(content: SKComponentViewProxy<*>, private val vertical: Boolean = true) :
    SKScreenViewProxy<TestScreenBinding>() {
    override val visibilityListener = dummyVisiblityListener()

    private val box = SKBoxViewProxy(
        itemsInitial = listOf(content),
        hiddenInitial = false,
    )


    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TestScreenBinding
    ): SKScreenView<TestScreenBinding> = SKTestScreenView(this, activity, fragment, binding).apply {
        if (!vertical) {
            binding.box.orientation = LinearLayout.HORIZONTAL
        }
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