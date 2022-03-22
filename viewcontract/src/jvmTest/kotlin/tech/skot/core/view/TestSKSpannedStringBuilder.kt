package tech.skot.core.view

import org.junit.Test
import kotlin.test.assertEquals

class TestSKSpannedStringBuilder {


    @Test
    fun testSimple() {

        val res = skSpannedString {
            colored(ColorRef(1)) {
                append("coucou")
                underline {
                    colored(ColorHex("#123456")) {
                        append("jaune")
                    }
                    striked {
                        append("rouge encore")
                    }
                }

            }
        }
        assertEquals(
            actual = res,
            expected = listOf(
                SKSpan(text = "coucou", format = SKSpan.Format(color = ColorRef(1))),
                SKSpan(text = "jaune", format = SKSpan.Format(underline = true, color = ColorHex("#123456"))),
                SKSpan(text = "rouge encore", format = SKSpan.Format(underline = true, striked = true, color = ColorRef(1))),
            )
        )

    }

    @Test
    fun testUnderline() {

        assertEquals(
            expected = listOf(SKSpan(text = "test1", SKSpan.Format(underline = true))),
            actual = skSpannedString {
                underline {
                    append("test1")
                }
            }
        )
    }

    @Test
    fun testStriked() {

        assertEquals(
            expected = listOf(SKSpan(text = "test1", SKSpan.Format(striked = true))),
            actual = skSpannedString {
                striked {
                    append("test1")
                }
            }
        )
    }
}