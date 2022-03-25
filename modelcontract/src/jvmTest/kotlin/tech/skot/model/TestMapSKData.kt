package tech.skot.model

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TestMapSKData {

    @Test
    fun testSimpleMap() {

        val original: SKManualData<Long> = SKManualData<Long>(0L)
        val rang1: SKData<String> = original.mapSuspend { it.toString() }
        val rang2: SKData<String> = rang1.mapSuspend { "Nombre : $it" }

        runBlocking {
            assertEquals(
                expected = rang1.get(),
                actual = "0"
            )
            assertEquals(
                expected = rang2.get(),
                actual = "Nombre : 0"
            )
            original.value = 1
            assertEquals(
                expected = rang1.get(),
                actual = "1"
            )
            assertEquals(
                expected = rang2.get(),
                actual = "Nombre : 1"
            )

        }
    }

    @Test
    fun testWithMutable() {
        val original: SKManualData<MutableSet<Long>> =
            SKManualData<MutableSet<Long>>(mutableSetOf())
        val rang1: SKData<List<String>> = original.mapSuspend { it.map { it.toString() } }
        val rang2: SKData<List<String>> = rang1.mapSuspend { it.map { "Nombre : $it" } }



        runBlocking {
            assertEquals(
                expected = rang1.get().size,
                actual = 0
            )
            assertEquals(
                expected = rang2.get().size,
                actual = 0
            )

            original.value.add(1)
            assertEquals(
                expected = rang1.get().size,
                actual = 1
            )
            assertEquals(
                expected = rang2.get().size,
                actual = 1
            )

        }

    }


}