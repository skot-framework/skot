package tech.skot.model

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test

class TestSKNullableManualDataWithCache {



    @Test
    fun testNullableSerialization() {
        val name = "NAME"
        val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

        runBlocking {
            val manual1 = object: SKManualDataWithCache<Nullable<String>>(
                name = name,
                serializer = Nullable.serializer(String.serializer()),
                cache = persistor,
                initialDefaultValue = Nullable("aa")
            ) {}
            assert(manual1.get().value == "aa")
            manual1.setValue(Nullable(null))
            assert(manual1.get().value == null)
            val value1 = "Value1"
            manual1.setValue(Nullable(value1))
            assert(manual1.get().value == value1)
            //La sauvegarde en cache est maintenant asynchrone alors on ajoute le délai
            delay(500)
            val manual2 = object:SKManualDataWithCache<Nullable<String>>(
                name = name,
                serializer = Nullable.serializer(String.serializer()),
                cache = persistor,
                initialDefaultValue = Nullable("bb")
            ) {}
            assert(manual2.get().value == value1)

            manual2.setValue(Nullable(null))
            val manual3 = object:SKManualDataWithCache<Nullable<String>>(
                name = name,
                serializer = Nullable.serializer(String.serializer()),
                cache = persistor,
                initialDefaultValue = Nullable("cc")
            ) {}
            assert(manual3.get().value == null)
        }
    }


    @Test
    fun testSKNullableManualDataWithCache() {
        val name = "NAME"
        val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")

        runBlocking {
            val manual1 = object: SKNullableManualDataWithCache<String>(
                name = name,
                dataSerializer = String.serializer(),
                cache = persistor,
                initialDefaultValue = "aa"
            ) {}
            assert(manual1.get() == "aa")
            manual1.setValue(null)
            assert(manual1.get() == null)
            val value1 = "Value1"
            manual1.setValue(value1)
            assert(manual1.get() == value1)
            //La sauvegarde en cache est maintenant asynchrone alors on ajoute le délai
            delay(500)
            val manual2 = object:SKNullableManualDataWithCache<String>(
                name = name,
                dataSerializer = String.serializer(),
                cache = persistor,
                initialDefaultValue = "bb"
            ) {}
            assert(manual2.get() == value1)

            manual2.setValue(null)
            val manual3 = object:SKNullableManualDataWithCache<String>(
                name = name,
                dataSerializer = String.serializer(),
                cache = persistor,
                initialDefaultValue = "cc"
            ) {}
            assert(manual3.get() == null)
        }
    }
}