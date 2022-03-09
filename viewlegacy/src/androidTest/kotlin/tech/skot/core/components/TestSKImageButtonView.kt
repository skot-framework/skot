package tech.skot.core.components

import android.R
import org.junit.Test
import tech.skot.core.SKLog
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.core.components.inputs.SKImageButtonViewProxy
import tech.skot.core.view.Icon
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponents

class TestSKImageButtonView : SKTestView() {

    @Test
    fun testDebounce() {

        var compteur500 = 0
        val button500 =
            SKImageButtonViewProxy(iconInitial = Icon(R.drawable.ic_delete), onTapInitial = {
                compteur500++
                SKLog.d("button500 -> $compteur500")
            })

        var compteurNoDebounce = 0
        val buttonNoDebounce =
            SKImageButtonViewProxy(iconInitial = Icon(R.drawable.ic_delete), debounce = null, onTapInitial = {
                compteurNoDebounce++
                SKLog.d("buttonNoDebounce -> $compteurNoDebounce")
            })

        var compteur2000 = 0
        val button2000 =
            SKImageButtonViewProxy(iconInitial = Icon(R.drawable.ic_delete), debounce = 2000, onTapInitial = {
                compteur2000++
                SKLog.d("button2000 -> $compteur2000")
            })

        testComponents(
            button500, buttonNoDebounce, button2000
        )

    }
}