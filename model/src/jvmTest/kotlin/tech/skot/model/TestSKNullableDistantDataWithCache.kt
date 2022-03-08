package tech.skot.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSKNullableDistantDataWithCache {

    val globalCache = JvmSKPersistor("global")

    @Test
    fun testBasic() {

        var manualValue: String? = "0"

        fun createSKData(): SKData<String?> = object : SKNullableDistantDataWithCache<String>(
            name = "ESSAI",
            dataSerializer = String.serializer(),
            key = null,
            cache = globalCache,
            validity = 100,
            fetchData = {
                manualValue
            }
        ) {}



        runBlocking {
            val skData1 = createSKData()
            assertTrue {
                skData1.get() == "0"
            }
            manualValue = "1"
            assertEquals(
                expected = "0",
                actual = skData1.get()
            )
            delay(100)
            assertEquals(
                expected = "1",
                actual = skData1.get()
            )
            manualValue = null
            delay(100)
            assertEquals(
                expected = null,
                actual = skData1.get()
            )

            val skData2 = createSKData()
            assertEquals(
                expected = null,
                actual = skData2.get()
            )
            manualValue = "3"
            assertEquals(
                expected = null,
                actual = skData2.get()
            )
            delay(100)
            assertEquals(
                expected = "3",
                actual = skData2.get()
            )

            val skData3 = createSKData()
            assertEquals(
                expected = "3",
                actual = skData3.fallBackValue()
            )
        }


    }
}