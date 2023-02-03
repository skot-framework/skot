package tech.skot.core.components

import org.junit.Test
import tech.skot.core.SKLog
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponents

class TestSKButtonView : SKTestView() {

    @Test
    fun testDebounce() {

        var compteur500 = 0
        val button500 =
            SKButtonViewProxy(labelInitial = "debounce standard (500ms)", onTapInitial = {
                compteur500++
                SKLog.d("button500 -> $compteur500")
            })

        var compteurNoDebounce = 0
        val buttonNoDebounce =
            SKButtonViewProxy(labelInitial = "no debounce", debounce = null, onTapInitial = {
                compteurNoDebounce++
                SKLog.d("buttonNoDebounce -> $compteurNoDebounce")
            })

        var compteur2000 = 0
        val button2000 =
            SKButtonViewProxy(labelInitial = "debounce long (2000ms)", debounce = 2000, onTapInitial = {
                compteur2000++
                SKLog.d("button2000 -> $compteur2000")
            })

        testComponents(
            button500, buttonNoDebounce, button2000
        )

    }
}