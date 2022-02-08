package tech.skot.model

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

class TestSKData {


    @Test
    fun `ManualSKData values are well setted and getted with value property`() {
        runBlocking {
            val manualSKData = SKManualData<Int>(0)

            assert(manualSKData.value == 0)
            manualSKData.value = 1
            assert(manualSKData.value == 1)
            launch {
                assert(manualSKData.get() == 1)
            }
        }
    }

    @Test
    fun `ManualSKData works well with nullable`() {
        runBlocking {
            val manualSKData = SKManualData<String?>(null)

            assert(manualSKData.value == null)
            manualSKData.value = "a"
            assert(manualSKData.value == "a")
            launch {
                assert(manualSKData.get() == "a")
                manualSKData.value = null
                assert(manualSKData.value == null)
            }

        }
    }


    @Test
    fun `ManualSKData flows works correctly`() {
        runBlocking {
            var counter = 0
            val manualSKData = SKManualData<Int>(counter)
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

    open class IncrementingDelayingSimpleSKData : SimpleSKData<Int>() {
        override val defaultValidity = 200L

        override suspend fun newDatedData(): DatedData<Int> {
            delay(100)
            return DatedData((_current?.data ?: -1) + 1)
        }
    }

    @Test
    fun `SimpleSKData concurrent updates`() {
        println("coucou !!")
        val testSK = IncrementingDelayingSimpleSKData()
        runBlocking {
            println(testSK.get())
            delay(300)
            val data1 = async { testSK.get() }
            val data2 = async { testSK.get() }

            assert(data1.await() == data2.await())

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
            delay(300L)
            assert(combined.get() == Pair(1,1))
            delay(300L)
            incr2.get()
            delay(300L)
            assert(combined.get() == Pair(2,3))
            assert(combined._current?.data == Pair(2,3))
        }
    }

    @Test
    fun `SKData combine get() are well parallelized`() {
        val simple1 = object: SimpleSKData<Int>() {
            override suspend fun newDatedData(): DatedData<Int> {
                delay(500)
                return DatedData(0)
            }
        }
        val simple2 = object: SimpleSKData<Int>() {
            override suspend fun newDatedData(): DatedData<Int> {
                delay(500)
                return DatedData(0)
            }
        }
        val combined = combineSKData(simple1, simple2)
        runBlocking {
            val duration = measureTimeMillis {
                assert(combined.get() == Pair(0,0))
            }
            assert(duration < 1000)

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


    @Test
    fun `SKDataWraper works correctly`() {

        val manual1 = SKManualData<String>("start1")
        val manual2 = SKManualData<String>("start2")

        val changeSK = SKManualData<Int>(0)

        var currentSK:SKData<String>? = manual1


        runBlockingTest {

            val wrapped = SKDataWrapper(
                defaultValue = "wrappedDefaultValue",
                getSKData = {
                    currentSK
                },
                newSKDataFlow = changeSK.flow,
                scope = this + Job()
            )

            assertEquals("start1", wrapped.get())
            manual1.value = "next1"
            assert(wrapped.get() == "next1")

            currentSK = manual2
            println("currentSK changed")
            assert(wrapped.get() == "next1")
            changeSK.value = 1
            assertEquals("start2", wrapped.get())

            currentSK = null
            changeSK.value = 2
            assertEquals("wrappedDefaultValue", wrapped.get())
            manual1.value = "next2"
            assertEquals("wrappedDefaultValue", wrapped.get())
        }

    }

}