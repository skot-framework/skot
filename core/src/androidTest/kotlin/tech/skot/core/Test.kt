package tech.skot.core

import org.junit.Test
import kotlin.test.assertEquals

class Test {

    @Test
    fun testShortHost() {
        val uri = "https://www.domain.com/seg1/seg2?param1=23&param2=EZR"
        println(uri)
        assertEquals(
            expected = "domain.com",
            actual = uri.toSKUri()?.shortHost
        )
    }
}