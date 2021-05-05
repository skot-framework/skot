package tech.skkot.model

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test
import tech.skot.model.SKDistantDataWithCache

class TestSKDistantDataWithCache {

    @Test
    fun testSKDistantDataWithCache() {

        val name = "Essai"
        val key = "key"

        val persistor = testPersistor()

        var compteur = 0
        val skData = SKDistantDataWithCache<Int>(
            name = name,
            serializer = Int.serializer(),
            key = key,
            cache = persistor
        ) {
            compteur++
        }

        runBlocking {
            assert(skData.get() == 0)
            assert(skData.get() == 0)
            assert(skData.get(validity = 0) == 1)

            val skData2 = SKDistantDataWithCache<Int>(
                name = name,
                serializer = Int.serializer(),
                key = key,
                cache = persistor
            ) {
                compteur++
            }

            assert(skData2.get() == 1)
        }

    }
}