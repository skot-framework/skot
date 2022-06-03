package tech.skot.core

import org.junit.Test
import kotlin.test.assertEquals

class TestSKDecimalFormat {

    @Test
    fun testFormat() {
       val format1 = SKDecimalFormat("#0")
       val format2 = SKDecimalFormat("#0.##")

        assertEquals(
            expected = "3",
            actual = format1.format(3.4)
        )

        assertEquals(
            expected = "4",
            actual = format1.format(3.6)
        )

        assertEquals(
            expected = "3,45",
            actual = format2.format(3.453)
        )

        assertEquals(
            expected = "13,45",
            actual = format2.format(13.45289048398)
        )
    }

    @Test
    fun testParse() {
        val format1 = SKDecimalFormat("#0")
        val format2 = SKDecimalFormat("#0.##")

        assertEquals(
            expected = 3.4,
            actual = format1.parse("3,4")
        )

        assertEquals(
            expected = 3.6,
            actual = format1.parse("3,6")
        )

        assertEquals(
            expected = 3.453,
            actual = format2.parse("3,453")
        )

        assertEquals(
            expected = 13.45289048398,
            actual = format2.parse("13,45289048398")
        )
    }

    @Test
    fun testAsPrice() {

        println(22.0.asPrice("EUR"))
        println(22.2.asPrice("EUR"))
        println(22.23.asPrice("EUR"))
        println(1464.0.asPrice("EUR"))

        println(22.0.asPriceTwoOrNoDigits("EUR"))
        println(22.2.asPriceTwoOrNoDigits("EUR"))
        println(22.23.asPriceTwoOrNoDigits("EUR"))
        println(1464.0.asPriceTwoOrNoDigits("EUR"))
    }


}