package tech.skot.core

import org.junit.Test
import kotlin.test.assertEquals

class TestSKUri {

    @Test
    fun testShortHost() {
        assertEquals(
            expected = "domain.com",
            actual = "https://www.domain.com/seg1/seg2?param1=23&param2=EZR".toSKUri()?.shortHost
        )

        assertEquals(
            expected = "domain.com",
            actual = "https://domain.com/seg1/seg2?param1=23&param2=EZR".toSKUri()?.shortHost
        )

        assertEquals(
            expected = "domain.com",
            actual = "https://sub2.sub.domain.com/seg1/seg2?param1=23&param2=EZR".toSKUri()?.shortHost
        )

        assertEquals(
            expected = "com",
            actual = "https://com/seg1/seg2?param1=23&param2=EZR".toSKUri()?.shortHost
        )
    }
}