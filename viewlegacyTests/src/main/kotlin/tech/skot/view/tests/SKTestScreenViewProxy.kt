package tech.skot.view.tests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.components.*
import tech.skot.viewlegacytests.databinding.TestScreenBinding
import tech.skot.view.tests.SKTestView.Companion.dummyVisiblityListener

class SKTestScreenViewProxy(content: SKComponentViewProxy<*>) :
    SKScreenViewProxy<TestScreenBinding>() {
    override val visibilityListener = dummyVisiblityListener()

    private val box = SKBoxViewProxy(
        itemsInitial = listOf(content),
        hiddenInitial = false
    )


    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TestScreenBinding
    ): SKScreenView<TestScreenBinding> = SKTestScreenView(this, activity, fragment, binding).apply {
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