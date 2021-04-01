package tech.skot.core.components

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.skot.model.DatedData
import tech.skot.model.SKData

class TestUseSKData {


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    open class IncrementingSimpleSKData : SKData<Int> {
        override val defaultValidity = 200L
        override val flow = MutableStateFlow<DatedData<Int>?>(null)
        override val _current
            get() = flow.value


        override suspend fun update(): Int {
            val newDatedValue = newDatedData()
            flow.value = newDatedValue
            return newDatedValue.data
        }
        suspend fun newDatedData(): DatedData<Int> {
            return DatedData((_current?.data ?: -1) + 1)
        }
    }

    @Test
    fun `Component SKData_onData`() {

        val incData = IncrementingSimpleSKData()

        val compo = object :Component<ComponentVC>() {
            override val view = object :ComponentVC {

            }



            private var counter = 0
            fun test() {
                launch {
                    incData.onData(withLoaderForFirstData = false) {
                        println("onData $it $counter")
                        counter++
                    }
                }
            }

            override fun treatError(exception: Exception, defaultErrorMessage: String?) {
                println("error ${exception.message}")
            }

        }

        compo.test()
        runBlocking {
            delay(1000)
            incData.update()
            delay(1000)
            incData.update()
        }


    }
}