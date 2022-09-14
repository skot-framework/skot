package tech.skot.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TestWrapSKData {

    class PorteurDeSKData {

        val skdata:SKManualData<Int> = SKManualData<Int>(0)

        fun increment() {
            skdata.value = skdata.value + 1
        }

    }

    @Test
    fun testWrapAvecUnMap() {

        val mere = SKManualData<PorteurDeSKData>(PorteurDeSKData())

        mere.value = PorteurDeSKData().also { it.increment() }
        runTest {
            val wrap:SKDataWrapper<Int> = mere.wrap(this, -1) {
                skdata.map { it }
            }
            assertEquals(
                expected = 1,
                actual = wrap.get()
            )
            wrap.cancel()
        }

    }


}