package tech.skot.core.components

import org.junit.Test
import tech.skot.core.components.inputs.SKInputVC
import tech.skot.core.components.inputs.SKInputViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponents

class TestSKInput: SKTestView() {


    @Test
    fun testInputYpes() {

        val proxyTextCapSentences = SKInputViewProxy(
            hintInitial = "TextCapSentences",
            type = SKInputVC.Type.TextCapSentences,
            textInitial = "",
            onInputText = {}
        )

        val proxyALLCAPS = SKInputViewProxy(
            hintInitial = "AllCaps",
            type = SKInputVC.Type.AllCaps,
            textInitial = "",
            onInputText = {}
        )


        testComponents(
            proxyTextCapSentences,
            proxyALLCAPS
        )

    }
}