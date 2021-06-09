package tech.skot.model

import org.junit.Test
import tech.skot.core.SKDateFormat

class TestSKDateFormat {

    @Test
    fun testParse() {
        val str = "09/04/1981 00:00:00+02:00"
        val skdf = SKDateFormat("dd/MM/yyyy HH:mm:ssXXX")

        val parsed = skdf.parse(str)
        assert(skdf.format(parsed) == str)
        println(parsed)
    }
}