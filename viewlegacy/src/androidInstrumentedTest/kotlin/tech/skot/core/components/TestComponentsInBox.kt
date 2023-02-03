package tech.skot.core.components

import kotlinx.coroutines.delay
import org.junit.Test
import tech.skot.core.components.inputs.SKComboVC
import tech.skot.core.components.inputs.SKInputViewProxy
import tech.skot.core.components.inputs.SKInputWithSuggestionsViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestComponentsInBox: SKTestView() {
    @Test
    fun testInABox() {
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


        val box = dummyBox(
            button,
            combo,
            imageButton,
            input,
            simpleInput,
            inputWithSuggestion,
            asItemVertical = true
        )

        testComponent(box) {
            delay(10000)
            box.items = listOf (
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