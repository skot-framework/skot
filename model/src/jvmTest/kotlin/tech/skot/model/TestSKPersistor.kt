package tech.skot.model

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestSKPersistor {

    @Test
    fun `test clear`() {

        val persistor = JvmSKPersistor("global")

        runTest {
            persistor.putBoolean("test1", true)

            assertTrue {
                persistor.getBoolean("test1", null) == true
            }

            persistor.clearSync()

            assertFalse {
                persistor.getBoolean("test1", null) == true
            }
        }


    }
}