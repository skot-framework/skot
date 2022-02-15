package tech.skot.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.serializer
import org.junit.Test
import tech.skot.core.SKLog
import java.lang.Exception
import kotlin.test.assertEquals

class TestBMsSKData: TestModel() {


    @Test
    fun testDistanDataWithLiveKeyCache() {
        val key = "key"
        var liveKey = 1
        var nbCalls = 0

        class TestBM: SKBM(key) {

            val distantData = DistantDataWithLiveKey<String>(
                   name = "Test",
                serializer = String.serializer(),
                validity = 5 * 60 * 1000,
                liveKey = { liveKey.toString() }
            ) {
                nbCalls++
                println("@@@@@@ distant call")
                "Coucou $liveKey"
            }
        }


        runBlocking {
            assertEquals(
                expected = "Coucou 1",
                actual = TestBM().distantData.get()
            )

            delay(200)
            launch {
                assertEquals(
                    expected = "Coucou 1",
                    actual = TestBM().distantData.get()
                )
                assertEquals(
                    expected = 1,
                    actual = nbCalls
                )
                delay(200)
                launch {
                    liveKey = 2
                    assertEquals(
                        message = "même si la validité n'est pas écoulée, la live-key ayant changé on doit recharger la donnée",
                        expected = "Coucou 2",
                        actual = TestBM().distantData.get()
                    )
                    assertEquals(
                        expected = 2,
                        actual = nbCalls
                    )
                }
            }

        }


    }

    @Test
    fun testDistanDataWithLiveKeyMemory() {
        val key = "key"
        var liveKey = 1
        var nbCalls = 0

        class TestBM: SKBM(key) {

            val distantData = DistantDataWithLiveKey<String>(
                name = "Test",
                serializer = String.serializer(),
                validity = 5 * 60 * 1000,
                liveKey = { liveKey.toString() }
            ) {
                nbCalls++
                println("@@@@@@ distant call")
                "Coucou $liveKey"
            }
        }


        runBlocking {
            val bm = TestBM()
            assertEquals(
                expected = "Coucou 1",
                actual = bm.distantData.get()
            )

            assertEquals(
                expected = "Coucou 1",
                actual = bm.distantData.get()
            )
            delay(200)
            assertEquals(
                expected = 1,
                actual = nbCalls
            )
            delay(200)
            liveKey = 2
            assertEquals(
                message = "même si la validité n'est pas écoulée, la live-key ayant changé on doit recharger la donnée",
                expected = "Coucou 2",
                actual = bm.distantData.get()
            )
            assertEquals(
                expected = 2,
                actual = nbCalls
            )

        }


    }

    @Test
    fun testDistanDataWithLiveKeyFallBack() {
        val key = "key"
        var liveKey = 1
        var nbCalls = 0
        var fail = false

        class TestBM: SKBM(key) {

            val distantData = DistantDataWithLiveKey<String>(
                name = "Test",
                serializer = String.serializer(),
                validity = 100,
                liveKey = { liveKey.toString() }
            ) {
                if (fail) {
                    throw Exception("Erreur")
                }
                else {
                    nbCalls++
                    println("@@@@@@ distant call")
                    "Coucou $liveKey"
                }

            }
        }

        suspend fun TestBM.getValueWithFallBack():String? {
            return try {
                distantData.get()
            }
            catch (ex:Exception) {
                distantData.fallBackValue()
            }
        }

        runBlocking {
            val bm = TestBM()
            assertEquals(
                expected = "Coucou 1",
                actual = bm.distantData.get()
            )
            delay(200)
            fail = true
            assertEquals(
                expected = "Coucou 1",
                actual = bm.getValueWithFallBack()
            )
            delay(200)
            liveKey = 2
            delay(200)
            assertEquals(
                expected = null,
                actual = bm.getValueWithFallBack()
            )
        }
    }

}