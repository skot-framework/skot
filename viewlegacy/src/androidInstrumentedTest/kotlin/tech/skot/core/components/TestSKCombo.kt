package tech.skot.core.components

import org.junit.Test
import tech.skot.core.components.inputs.SKComboVC
import tech.skot.core.components.inputs.SKComboViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestSKCombo: SKTestView() {


    @Test
    fun testSKComboStyle() {

        val skCombo1 = SKComboViewProxy(
           hint = "hint1",
            choicesInitial = listOf(
                SKComboVC.Choice("inputWithSuggestion data 1"),
                SKComboVC.Choice("inputWithSuggestion data 2")
            )
        )

        val skCombo2 = SKComboViewProxy(
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
                skCombo1,
                skCombo2
            )
        )


        testComponent(box)

    }

}