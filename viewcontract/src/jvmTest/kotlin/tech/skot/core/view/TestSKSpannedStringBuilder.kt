package tech.skot.core.view

import org.junit.Test
import kotlin.test.assertEquals

class TestSKSpannedStringBuilder {


    @Test
    fun testSimple() {

        val res = skSpannedString {
            colored(Color(1)) {
                append("coucou")
                underline {
                    colored(Color(2)) {
                        append("jaune")
                    }
                    striked {
                        append("rouge encore")
                    }
                }

            }

        }
        println(res)

    }

    @Test
    fun testUnderline() {

        assertEquals(
            expected = listOf(SKSpan(text = "test1", underline = true)),
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
            expected = listOf(SKSpan(text = "test1", striked = true)),
            actual = skSpannedString {
                striked {
                    append("test1")
                }
            }
        )
    }
}