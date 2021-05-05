package tech.skot.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import org.junit.Test
import kotlin.test.assertFails

class TestSKPersistor {

    @Test
    fun `Test getData secured or not`() {
        val failingPersistor = object : SKPersistor {
            override suspend fun <D : Any> putData(serializer: KSerializer<D>, name: String, data: D, key: String?, timestamp: Long) {
                throw IllegalStateException("crash !")
            }

            override suspend fun <D : Any> getData(serializer: KSerializer<D>, name: String, key: String?): DatedData<D>? {
                throw IllegalStateException("crash !")
            }

            override suspend fun putString(name: String, data: String, key: String?, timestamp: Long) {
                throw IllegalStateException("crash !")
            }

            override suspend fun getString(name: String, key: String?): DatedData<String>? {
                throw IllegalStateException("crash !")
            }

            override suspend fun remove(name: String) {
                throw IllegalStateException("crash !")
            }

            override suspend fun clear() {
                throw IllegalStateException("crash !")
            }

        }

        runBlocking {
            assertFails {
                failingPersistor.getString("Coucou")
            }
            failingPersistor.getStringSecured("test")
            delay(1000)
        }


    }
}