package tech.skkot.model

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import tech.skot.model.AndroidPersistor
import kotlin.test.assertTrue

suspend fun assertSuspendTrue(block:suspend ()->Boolean) {
    val res = block()
    assertTrue { res }
}

class TestPersistor {

    @ExperimentalCoroutinesApi
    @Test
    fun testSimple() {


        val persistor = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")
        runBlockingTest {
            assertSuspendTrue { persistor.getString("test") == null }
        }


    }
}