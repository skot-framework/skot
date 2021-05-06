package tech.skkot.model

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
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

            //La sauvegarde en cache est maintenant asynchrone alors on ajoute le délai
            delay(500)
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

    @Serializable
    data class Data1(val champ1:String, val champ2:Int)

    @Serializable
    data class Data1mod(val champ1:String, val champ2mod:Int)


    @Test
    fun testModelChangeProof() {



        val name = "NAME"
        val val1 = "val1"
        val val2 = 2
        val val1mod = "val1Mod"
        val val2mod = 3

        val cache = testPersistor()

        runBlocking {
            val skData1 = SKDistantDataWithCache<Data1>(
                name = name,
                serializer = Data1.serializer(),
                cache = cache,
            ) {
                Data1(val1, val2)
            }

            assert(skData1.get() == Data1(val1, val2))
            //Délai car mise en cache asynchrone
            delay(500)
            val skData1_2 = SKDistantDataWithCache<Data1>(
                name = name,
                serializer = Data1.serializer(),
                cache = cache,
                validity = 2000L
            ) {
                Data1("dd", 45)
            }
            assert(skData1_2.get() == Data1(val1, val2))

            val skData1_mod = SKDistantDataWithCache<Data1mod>(
                name = name,
                serializer = Data1mod.serializer(),
                cache = cache,
                validity = 2000L
            ) {
                Data1mod(val1mod, val2mod)
            }
            assert(skData1_mod.get() == Data1mod(val1mod, val2mod))
        }
    }


}