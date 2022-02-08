package tech.skot.model

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Test

class TestSerialization {

    @Serializable
    class A(val int1:Int, val str1:String)

    @Serializable
    class AMod(val str1:String, val long1:Long? = null)

    @Test
    fun testClassModification() {

        runBlocking {
            val a1 = A(
                int1 = 1,
                str1 = "chaine1")

            val json = Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }

            val ser1 = json.encodeToString(A.serializer(), a1)



            val a1Deser = json.decodeFromString(A.serializer(), ser1)
            assert(a1Deser.int1 == a1.int1)
            assert(a1Deser.str1 == a1.str1)

            val a1ModDeser = json.decodeFromString(AMod.serializer(), ser1)

            assert(a1ModDeser.str1 == a1.str1)
        }

    }
}