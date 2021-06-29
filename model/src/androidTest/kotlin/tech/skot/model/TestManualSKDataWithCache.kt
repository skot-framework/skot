package tech.skot.model

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test

class TestManualSKDataWithCache {

    companion object {
        const val INIT_VALUE = "init"
    }
    @Test
    fun testCacheValueUsed() {
        val name = "NAME"
        val persistor = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, "testpersistor")



        runBlocking {
            val manual1 = object: SKManualDataWithCache<String>(
                name = name,
                serializer = String.serializer(),
                cache = persistor,
                initialDefaultValue = INIT_VALUE
            ) {}
            assert(manual1.get() == INIT_VALUE)
            val value1 = "Value1"
            manual1.setValue(value1)
            assert(manual1.get() == value1)
            //La sauvegarde en cache est maintenant asynchrone alors on ajoute le délai
            delay(500)
            val manual2 = object:SKManualDataWithCache<String>(
                name = name,
                serializer = String.serializer(),
                cache = persistor,
                initialDefaultValue = INIT_VALUE
            ) {}
            assert(manual2.get() == value1)
        }

    }
}