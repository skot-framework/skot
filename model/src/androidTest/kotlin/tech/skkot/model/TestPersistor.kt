package tech.skkot.model

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.Test
import tech.skot.model.AndroidPersistor
import kotlin.test.assertTrue


class TestPersistor {

    @Test
    fun testBasicKeyStringJobs() {
        runBlocking {
            val persistor = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

            val key = "test"
            val initialValue = persistor.getString(key)
            assertTrue("Initial value is null") {
                initialValue == null
            }
            val settedData = "Hello !"
            persistor.putString(key, settedData)
            val savedValue = persistor.getString(key)
            assertTrue("Put data is well saved") {
                savedValue?.data == settedData
            }

            val secondKey = "keyTest2"
            val settedSecondData = "Hello test 2"
            persistor.putString(secondKey, settedSecondData)
            persistor.getString(key).let {
                assertTrue("First key value still ok") {
                    it?.data == settedData
                }
            }
            persistor.getString(secondKey).let {
                assertTrue("Second Key value is well saved") {
                    it?.data == settedSecondData
                }
            }

        }
    }


    @Test
    fun testIdsStringJobs() {
        runBlocking {
            val persistor = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

            val key1 = "key1"
            val value1 = "value1"

            val key2 = "key2"
            val value2 = "value2"
            val id2 = "id2"

            persistor.putString(key1, value1)
            persistor.getString(key1).let {
                assertTrue("Default id is null") {
                    it?.id == null
                }
            }

            persistor.putString(key2, id2, value2)
            persistor.getString(key2).let {
                assertTrue("Id is well setted") {
                    it?.id == id2
                }
            }
            persistor.getString(key1).let {
                assertTrue("Id from other key are not affected") {
                    it?.id == null
                }
            }
        }
    }


    @Test
    fun testPersistedOnDisk() {
        runBlocking {
            val key = "key"
            val value = "value"
            val fileName = "testPersistent"

            val persistor1 = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(key, value)

            val persistor2 = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor2.getString(key).let {
                assertTrue("New Persistor with same file name find value saved before") {
                    it?.data == value
                }
            }
        }
    }

    @Test
    fun testClearAll() {
        runBlocking {
            val key = "key"
            val value = "value"
            val fileName = "testClear"

            val persistor1 = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(key, value)

            persistor1.getString(key).let {
                assertTrue("value well saved") {
                    it?.data == value
                }
            }
            persistor1.clear()
            persistor1.getString(key).let {
                assertTrue("data well cleared") {
                    it == null
                }
            }

            persistor1.putString(key, value)

            persistor1.getString(key).let {
                assertTrue("value well saved") {
                    it?.data == value
                }
            }
            persistor1.clear()

            val persistor2 = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor2.getString(key).let {
                assertTrue("New Persistor with same file name don't find cleared value") {
                    it == null
                }
            }
        }
    }

    @Test
    fun testRemoveOneKey() {
        runBlocking {
            val key = "key"
            val value = "value"
            val key2 = "key2"
            val value2 = "value2"

            val fileName = "testClear"

            val persistor1 = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(key, value)
            persistor1.putString(key2, value2)

            persistor1.getString(key).let {
                assertTrue("value well saved") {
                    it?.data == value
                }
            }
            persistor1.getString(key2).let {
                assertTrue("value2 well saved") {
                    it?.data == value2
                }
            }
            persistor1.remove(key)
            persistor1.getString(key).let {
                assertTrue("data well cleared") {
                    it == null
                }
            }
            persistor1.getString(key2).let {
                assertTrue("value2 not affected") {
                    it?.data == value2
                }
            }

        }
    }

    @Serializable
    data class SubObject(val oneField: String)

    @Serializable
    data class TestObject(val field1: String, val field2: Int, val field3: SubObject)


    @Test
    fun testSavingObjects() {
        runBlocking {
            val key = "key"
            val value = TestObject(
                    field1 = "test",
                    field2 = 3,
                    field3 = SubObject("coucou")
            )
            val fileName = "testSavingObjects"
            val persistor = AndroidPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)

            persistor.putData(TestObject.serializer(), key, value)

            persistor.getData(TestObject.serializer(), key).let {
                assertTrue {
                    it?.data == value
                }
            }
        }
    }

}