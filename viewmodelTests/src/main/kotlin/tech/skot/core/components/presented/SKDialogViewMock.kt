package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock
import tech.skot.core.components.SKScreenVC
import kotlin.reflect.KClass
import kotlin.test.assertTrue

class SKDialogViewMock : SKComponentViewMock(), SKDialogVC {
    override var state: SKDialogVC.Shown? = null
}

fun SKDialogVC.assertDisplay(rule: String = "", screenClass: KClass<out SKScreenVC>) {

    assertTrue(rule + " -> on doit afficher une modale de dialoque") {
        state?.screen != null
    }

    assertTrue(
        rule + " -> on doit afficher une modale de dialoque de type ${screenClass.simpleName}"
    ) {
        state?.screen?.let {
            screenClass.isInstance(it)
        } ?: false
    }

}