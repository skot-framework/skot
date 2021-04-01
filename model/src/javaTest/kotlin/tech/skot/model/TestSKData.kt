package tech.skot.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestSKData {


    @Test
    fun `ManualSKData values are well setted and getted with value property`() {
        runBlocking {
            val manualSKData = ManualSKData<Int>(0)

            assert(manualSKData.value == 0)
            manualSKData.value = 1
            assert(manualSKData.value == 1)
            launch {
                assert(manualSKData.get() == 1)
            }
        }


    }

    @Test
    fun `ManualSKData flows works correctly`() {
        runBlocking {
            var counter = 0
            val manualSKData = ManualSKData<Int>(counter)
            val collecting = launch {
                manualSKData.flow.collect {
                    println("collected : $it (counter = $counter)")
                    assert(it?.data == counter)
                }
            }
            delay(100)
            repeat(4) {
                manualSKData.value = ++counter
                delay(100)
            }
            collecting.cancel()
        }
    }


    open class IncrementingSimpleSKData : SimpleSKData<Int>() {
        override val defaultValidity = 200L

        override suspend fun newDatedData(): DatedData<Int> {
            return DatedData((_current?.data ?: -1) + 1)
        }
    }


    @Test
    fun `SimpleSKData get() fun get updated version`() {
        val testSK = IncrementingSimpleSKData()
        runBlocking {
            assert(testSK.get() == 0)
            assert(testSK.get() == 0)
            delay(201L)
            assert(testSK.get() == 1)
            delay(201L)
            assert(testSK.get() == 2)
        }
    }


    @Test
    fun `SimpleSKData flows works correctly`() {
        val testSK = IncrementingSimpleSKData()
        runBlocking {
            var counter = -1
            val collecting = launch {
                testSK.flow.collect {
                    if (counter == -1) {
                        assert(it == null)
                    } else {
                        assert(it?.data == counter)
                    }
                    counter++
                }
            }
            delay(50L)
            testSK.update()
            delay(200L)
            testSK.update()
            delay(200L)
            testSK.update()
            delay(200L)
            testSK.update()
            collecting.cancel()
        }
    }


    @Test
    fun `SKData map values are well mapped and flow is ok`(){
        val incSKData = IncrementingSimpleSKData()
        val mappedSKData = incSKData.map {
            "line $it"
        }
        runBlocking {
            assert(mappedSKData.get() == "line 0")
            assert(mappedSKData._current?.data == "line 0")
            delay(200L)
            assert(mappedSKData.get() == "line 1")
            var counter = 1
            val collecting = launch {
                mappedSKData.flow.collect {
                    assert(it?.data == "line $counter")
                    counter++
                }
            }
            delay(200L)
            mappedSKData.update()
            delay(200L)
            mappedSKData.update()
            delay(50)
            collecting.cancel()
        }
    }

    @Test
    fun `SKData map validity is well transmitted`(){
        val incSKData = IncrementingSimpleSKData()
        val mappedSKData = incSKData.map {
            "line $it"
        }
        runBlocking {
            assert(mappedSKData.get() == "line 0")
            delay(100L)
            assert(mappedSKData.get() == "line 0")
            delay(100L)
            assert(mappedSKData.get() == "line 1")

        }
    }



    @Test
    fun `SKData combine value and get() are well computed`() {
        val incr1 = IncrementingSimpleSKData()
        val incr2 = IncrementingSimpleSKData()
        val combined = combineSKData(incr1, incr2)
        runBlocking {
            assert(combined.get() == Pair(0,0))
            delay(200L)
            assert(combined.get() == Pair(1,1))
            delay(200L)
            incr2.get()
            delay(200L)
            assert(combined.get() == Pair(2,3))
            assert(combined._current?.data == Pair(2,3))
        }
    }



    @Test
    fun `SKData combine validity is well managed`() {
        val incr1 = IncrementingSimpleSKData()
        val incr2 = object :IncrementingSimpleSKData() {
            override val defaultValidity = 300L
        }
        val combined = combineSKData(incr1, incr2)

        runBlocking {
            assert(combined.get() == Pair(0,0))
            delay(100L)
            assert(combined.get() == Pair(0,0))
            delay(120L)
            assert(combined.get() == Pair(1,0))
            delay(100L)
            assert(combined.get() == Pair(1,1))
        }
    }

    @Test
    fun `SKData combine extension`() {
        val incr1 = IncrementingSimpleSKData()
        val incr2 = IncrementingSimpleSKData()
        val combined = incr1.combine(incr2)

        runBlocking {
            assert(combined.get() == Pair(0,0))
            delay(100L)
            assert(combined.get() == Pair(0,0))
            delay(101L)
            assert(combined.get() == Pair(1,1))
            delay(100L)
            assert(combined.get() == Pair(1,1))
        }
    }
}