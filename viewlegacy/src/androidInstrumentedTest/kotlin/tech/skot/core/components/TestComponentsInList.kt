package tech.skot.core.components

import kotlinx.coroutines.delay
import org.junit.Test
import tech.skot.core.components.inputs.SKComboVC
import tech.skot.core.components.inputs.SKInputViewProxy
import tech.skot.core.components.inputs.SKInputWithSuggestionsViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestComponentsInList : SKTestView() {

    @Test
    fun testInAList() {
        val button = dummyButton(toast = "Coucou button", label = "button")
        val combo = dummyCombo(selected = "selected")
        val imageButton = dummyImageButton(toast = "Coucou ImageButton")
        val input = SKInputViewProxy(onInputText = {
            toast("input $it")()
        })
        val simpleInput = dummySimpleInput(text = "simple input", hint = "hint simple input")
        val inputWithSuggestion = SKInputWithSuggestionsViewProxy(
            onInputText = {
                toast("input $it")()
            },
            choicesInitial = listOf(
                SKComboVC.Choice("inputWithSuggestion data 1"),
                SKComboVC.Choice("inputWithSuggestion data 2")
            )
        )


        val list = dummyList(
            button,
            combo,
            imageButton,
            input,
            simpleInput,
            inputWithSuggestion
        )

        testComponent(list) {
            delay(10000)
            list.setItems(
                inputWithSuggestion,
                simpleInput,
                input,
                imageButton,
                combo,
                button,
            )
        }
    }

}