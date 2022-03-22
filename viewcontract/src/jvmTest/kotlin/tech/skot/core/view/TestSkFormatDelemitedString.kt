package tech.skot.core.view

import org.junit.Test
import kotlin.test.assertEquals

class TestSkFormatDelemitedString {

    @Test
    fun testZones() {
        val input = "##JE PROLONGE## MES AVANTAGES POUR :"
        val spanned = input.skFormatDelemitedString(
            SKSpan.Format(color = ColorHex("#23ee45")),
            SKSpan.Format(typeface = SKSpan.Bold),
        )

        assertEquals(
            actual = spanned,
            expected = listOf(
                SKSpan(text = "JE PROLONGE", format = SKSpan.Format(color = ColorHex("#23ee45"))),
                SKSpan(
                    text = " MES AVANTAGES POUR :",
                    format = SKSpan.Format(typeface = SKSpan.Bold)
                )
            )
        )
    }


}