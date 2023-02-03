package tech.skot.core.components

import org.junit.Test
import tech.skot.core.components.inputs.SKComboVC
import tech.skot.core.components.inputs.SKInputWithSuggestionsViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestSKInputWithSuggestions: SKTestView() {


    @Test
    fun testSKInputWithSuggestionsStyle() {

        val skInput1 = SKInputWithSuggestionsViewProxy(
            onInputText = {
                toast("input $it")()
            },
            hint = "hint1",
            choicesInitial = listOf(
                SKComboVC.Choice("inputWithSuggestion data 1"),
                SKComboVC.Choice("inputWithSuggestion data 2")
            )
        )

        val skInput2 = SKInputWithSuggestionsViewProxy(
            onInputText = {
                toast("input $it")()
            },
            hint = "hint2",
            choicesInitial = listOf(
                SKComboVC.Choice("inputWithSuggestion data 1"),
                SKComboVC.Choice("inputWithSuggestion data 2")
            ),
            oldSchoolModeHint = true
        )


        val box = SKBoxViewProxy(
            asItemVertical = true,
            itemsInitial = listOf(
                skInput1,
                skInput2
            )
        )


        testComponent(box)

    }

}