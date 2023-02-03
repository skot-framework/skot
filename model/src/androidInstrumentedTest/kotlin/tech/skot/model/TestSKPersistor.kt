package tech.skot.model

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertTrue
import kotlinx.serialization.Serializable

class TestSKPersistor {

    @Test
    fun testBasicKeyStringJobs() {
        runBlocking {
            val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

            val name = "test"
            val initialValue = persistor.getString(name)
            assertTrue("Initial value is null") {
                initialValue == null
            }
            val settedData = "Hello !"
            persistor.putString(name, settedData)
            val savedValue = persistor.getString(name)
            assertTrue("Put data is well saved") {
                savedValue == settedData
            }

            val secondName = "nameTest2"
            val settedSecondData = "Hello test 2"
            persistor.putString(secondName, settedSecondData)
            persistor.getString(name).let {
                assertTrue("First name value still ok") {
                    it == settedData
                }
            }
            persistor.getString(secondName).let {
                assertTrue("Second name value is well saved") {
                    it == settedSecondData
                }
            }

        }
    }


    @Test
    fun testStringJobsWithKeys() {
        runBlocking {
            val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

            val name1 = "name1"
            val value1 = "value1"

            val anotherKey = "anotherKey"

            val value2 = "value2"

            persistor.putString(name1, value1)
            persistor.getString(name = name1, key = null).let {
                assertTrue("Default id is null") {
                    value1 == it
                }
            }

            persistor.getString(name = name1, key = anotherKey).let {
                assertTrue("Wrong key return null") {
                    it == null
                }
            }

            persistor.putString(name1, value2, anotherKey)
            persistor.getString(name = name1, key = null).let {
                assertTrue("Setting value with new key old key -> nulll") {
                    it == null
                }
            }

            persistor.getString(name = name1, key = anotherKey).let {
                assertTrue("new key -> good value") {
                    it == value2
                }
            }

        }
    }


    @Test
    fun testPersistedOnDisk() {
        runBlocking {
            val name = "name"
            val value = "value"
            val fileName = "testPersistent"

            val persistor1 = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(name, value)

            val persistor2 = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor2.getString(name).let {
                assertTrue("New Persistor with same file name find value saved before") {
                    it == value
                }
            }
        }
    }



    @Test
    fun testClearAll() {
        runBlocking {
            val name = "name"
            val value = "value"
            val fileName = "testClear"

            val persistor1 = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(name, value)

            persistor1.getString(name).let {
                assertTrue("value well saved") {
                    it == value
                }
            }
            persistor1.clear()
            persistor1.getString(name).let {
                assertTrue("data well cleared") {
                    it == null
                }
            }

            persistor1.putString(name, value)

            persistor1.getString(name).let {
                assertTrue("value well saved") {
                    it == value
                }
            }
            persistor1.clear()

            val persistor2 = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor2.getString(name).let {
                assertTrue("New Persistor with same file name don't find cleared value") {
                    it == null
                }
            }
        }
    }

    @Test
    fun testRemoveOneKey() {
        runBlocking {
            val name = "name"
            val value = "value"
            val name2 = "name2"
            val value2 = "value2"

            val fileName = "testClear"

            val persistor1 = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)
            persistor1.putString(name, value)
            persistor1.putString(name2, value2)

            persistor1.getString(name).let {
                assertTrue("value well saved") {
                    it == value
                }
            }
            persistor1.getString(name2).let {
                assertTrue("value2 well saved") {
                    it == value2
                }
            }
            persistor1.remove(name)
            persistor1.getString(name).let {
                assertTrue("data well cleared") {
                    it == null
                }
            }
            persistor1.getString(name2).let {
                assertTrue("value2 not affected") {
                    it == value2
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
            val name = "name"
            val value = TestObject(
                    field1 = "test",
                    field2 = 3,
                    field3 = SubObject("coucou")
            )

            val key = "key"
            val otherKey = "otherKey"

            val fileName = "testSavingObjects"
            val persistor:SKPersistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, fileName)

            persistor.putData(TestObject.serializer(), name, value)

            persistor.getData(TestObject.serializer(), name).let {
                assertTrue {
                    it == value
                }
            }

            persistor.getData(TestObject.serializer(), name, key = key).let {
                assertTrue {
                    it == null
                }
            }

            persistor.getData(TestObject.serializer(), name).let {
                assertTrue {
                    it == value
                }
            }

            persistor.putData(TestObject.serializer(), name, value, key)
            persistor.getData(TestObject.serializer(), name, key).let {
                assertTrue {
                    it == value
                }
            }
            persistor.getData(TestObject.serializer(), name).let {
                assertTrue {
                    it == null
                }
            }
            persistor.getData(TestObject.serializer(), name, otherKey).let {
                assertTrue {
                    it == null
                }
            }

        }
    }

    @Serializable
    data class Data1(val field1:String, val field2:Int)

    @Serializable
    data class Data1Mod(val field1:String, val field2:String)

    @Test
    fun testChangingModel() {


        val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testChangingModel")

        val name = "NAME"

        val data1 = Data1("test", 4)
        val data1Mod = Data1Mod("test", "4")
        runBlocking {
            persistor.putData(Data1.serializer(), name, data1)

            assert(persistor.getData(Data1.serializer(), name) == data1)
            assert(persistor.getData(Data1Mod.serializer(), name) == null)
        }
    }




    @Serializable
    open class TestSer(open val test:String)

    class TestSerImpl(override var test: String):TestSer(test)

    @Test
    fun testSerialization() {
        val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testSerialization")
        val name = "TEST"

        runBlocking {
            val essai = TestSerImpl(test = "avant")
            essai.test = "après"
            persistor.putData(
                serializer = TestSer.serializer(),
                name = name,
                essai
            )

            val restored =
                persistor.getData(
                    serializer = TestSer.serializer(),
                    name = name
                )

            assert(restored?.test == essai.test)
        }
    }


    @Test
    fun testBlobLimit() {

        runBlocking {
            val testBigStr = "@".repeat(3000000)

            val cache = testPersistor("testBlobLimit")
            cache.putString("testBlobLimit", testBigStr)
            assert(cache.getString("testBlobLimit") == testBigStr)
        }


    }

    @Test
    fun testDataStoreNoLimit() {
        runBlocking {
            val testBigStr = "@".repeat(10000000)
            val cache = testPersistor("testBlobLimit")
            cache.putString("testBlobLimit", testBigStr)

            assert(cache.getString("testBlobLimit") == testBigStr)

            delay(1000)



//            val sb = StringBuilder()
//            (1..2000000).forEach {
//                sb.append("1234567890")
//            }
//            Log.d("SKOT",sb.toString())


//            assert(cache.getStringValue("testBlobLimit") == testBigStr)
//
//            val testBigStrTooBig = "@".repeat(3000000)
//            cache.putString("testBlobLimit", testBigStrTooBig)
//            assert(cache.getString("testBlobLimit")?.data == null)
        }
    }

}