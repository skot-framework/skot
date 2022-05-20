package tech.skot.model

import io.ktor.util.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSKLruMemoryCache {

    class SimpleCacheTest(dataValidity:Long? = null) {
        var nbLoad = 0

        val cache = SKLruMemoryCache<String, String>(4, dataValidity) {
            delay(100)
            nbLoad++
            it.toUpperCase()
        }
    }


    private fun simpleTest(dataValidity:Long? = null, work: SimpleCacheTest.() -> Unit) {
        SimpleCacheTest(dataValidity).apply {
            work.invoke(this)
        }
    }

    @Test
    fun `getting data even in parrallel is ok`() {
        simpleTest {
            runBlocking {
                val aaData = cache.get("aa")
                assertTrue { aaData == "AA" }

                assertTrue { nbLoad == 1 }

                val bbData = cache.get("bb")
                assertTrue { bbData == "BB" }

                assertTrue { nbLoad == 2 }
                cache.get("bb")
                assertTrue { nbLoad == 2 }

                cache.get("cc")
                cache.get("cc")

                assertTrue { nbLoad == 3 }

                joinAll(
                        launch(Dispatchers.Default) {
                            println(measureTimeMillis {
                                println("first start")
                                cache.get("dd")
                                println("first end")
                            })


                        },
                        launch(Dispatchers.Default) {
                            println(measureTimeMillis {
                                println("second start")
                                cache.get("dd")
                                println("second end")
                            })
                        }
                )
                assertTrue("parralel get give no double refresh") { nbLoad == 4 }

            }
        }

    }


    @Test
    fun `slot are well used with just one access`() {
        simpleTest {
            runBlocking {
                cache.get("1")
                cache.get("2")
                cache.get("3")
                cache.get("4")
                cache.get("5")
                assertTrue { nbLoad == 5 }
                cache.get("1")
                assertTrue { nbLoad == 6 }
                cache.get("5")
                assertTrue { nbLoad == 6 }
                cache.get("5")
                assertTrue { nbLoad == 6 }
            }
        }
    }

    @Test
    fun `slot are well used with with access`() {
        simpleTest {
            runBlocking {
                cache.get("1")
                cache.get("2")
                cache.get("3")
                cache.get("1")
                cache.get("4")
                cache.get("5")
                assertTrue { nbLoad == 5 }
                cache.get("1")
                assertTrue { nbLoad == 5 }
                cache.get("5")
                assertTrue { nbLoad == 5 }
                cache.get("5")
                assertTrue { nbLoad == 5 }
                cache.get("2")
                assertTrue { nbLoad == 6 }
            }
        }
    }

    @Test
    fun `validity`() {
        simpleTest(500) {
            runBlocking {
                cache.get("1")
                cache.get("1")
                assertTrue { nbLoad == 1 }
                delay(500)
                cache.get("1")
                assertTrue { nbLoad == 2 }
            }
        }
    }


    @Test
    fun `test ok with nullable data`() {
        var nbLoad = 0
        val cache = SKLruMemoryCache<Int, String?>(4, 500) {
            nbLoad++
            delay(100)
            if (it%2 == 0) {
                null
            }
            else {
                it.toString()
            }

        }
        runBlocking {
            assertEquals(
                actual = cache.get(1),
                expected = "1"
            )
            assertEquals(
                actual = cache.get(2),
                expected = null
            )
            assertEquals(
                actual = cache.get(3),
                expected = "3"
            )
            assertEquals(
                actual = nbLoad,
                expected = 3
            )
            cache.get(1)
            assertEquals(
                actual = nbLoad,
                expected = 3
            )
            cache.get(4)
            cache.get(5)
            assertEquals(
                actual = nbLoad,
                expected = 5
            )
            cache.get(1)
            assertEquals(
                actual = nbLoad,
                expected = 6
            )
        }
    }


}