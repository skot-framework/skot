package tech.skot.core.components

import kotlinx.coroutines.delay
import org.junit.Test
import tech.skot.core.components.inputs.SKInputViewProxy
import tech.skot.core.components.inputs.SKInputWithSuggestionsViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent
import tech.skot.viewlegacy.test.R

class TestBoxView : SKTestView() {

    @Test
    fun testSKBox() {
        SKInputViewProxy.LAYOUT_ID = R.layout.sk_input_test
        val input1 = dummyInput(hint = "hint1", text = "text1")
        val input2 = dummyInput(hint = "hint2", text = "text2")
        val input3 = dummyInput(hint = "hint3", text = "text3")

        val inputwithSuggestions = SKInputWithSuggestionsViewProxy(
            hint = "hintwith sugg1",
            errorInitial = null,
            onSelected = null,
            choicesInitial = emptyList(),
            selectedInitial = null,
            enabledInitial = true,
            hiddenInitial = false,
            dropDownDisplayedInitial = false,
            onInputText = {}
        )
        val proxyBox = SKBoxViewProxy(
            itemsInitial = listOf(input1, input2, input3, inputwithSuggestions),
            hiddenInitial = false,
            asItemVertical = true
        )

        testComponent(proxyBox) {
            delay(8000)
            proxyBox.items = listOf(input3, input2)
            delay(4000)
            proxyBox.items = listOf(input1, input2, input3, inputwithSuggestions)
            delay(6000)
            input1.text = "Coucou"
        }

    }
}