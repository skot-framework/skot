package tech.skkot.model

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.Test

class TestPersistSealedClass {

    @Serializable
    sealed class State {
        @Serializable
        data class InStore(val storeName: String) : State()
        @Serializable
        object NoSession : State()
    }


    @Test
    fun testPersistSealedClass() {

        val cache = testPersistor()
        val name = "NAME"
        val MAG_1 = "Mag 1"
        runBlocking {
            cache.putData(
                State.serializer(),
                name,
                State.InStore(MAG_1)
            )

            val restoredDataInStore = cache.getData(
                State.serializer(),
                name
            )?.data

            assert(restoredDataInStore is State.InStore)
            assert((restoredDataInStore as State.InStore).storeName == MAG_1)

            cache.putData(
                State.serializer(),
                name,
                State.NoSession
            )

            val restoreDataNoSession = cache.getData(
                State.serializer(),
                name
            )?.data

            assert(restoreDataNoSession is State.NoSession)
        }
    }
}