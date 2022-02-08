package tech.skot.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestSKDistantData {


    @Test
    fun `SKDistantData base `() {
        var compteur = 0

        val skDistantDataTest = SKDistantData<Int> {
            compteur++
        }

        runBlocking {
            assert(skDistantDataTest.get() == 0)
            delay(200)
            assert(skDistantDataTest.get() == 0)
            assert(skDistantDataTest.get(validity = 0) == 1)
        }
    }

    @Test
    fun `SKDistantData validity `() {
        var compteur = 0

        val skDistantDataTest = SKDistantData<Int>(validity = 100) {
            compteur++
        }

        runBlocking {
            assert(skDistantDataTest.get() == 0)
            delay(200)
            assert(skDistantDataTest.get() == 1)
            assert(skDistantDataTest.get(validity = 0) == 2)
        }
    }
}