package tech.skot.core.view

import org.junit.Test

class TestSKSpannedStringBuilder {


    @Test
    fun testSimple() {

        val res = skSpannedString {
            colored(Color(1)) {
                append("coucou")
                colored(Color(2)) {
                    append("jaune")
                }
                append("rouge encore")
            }

        }
        println(res)

    }
}