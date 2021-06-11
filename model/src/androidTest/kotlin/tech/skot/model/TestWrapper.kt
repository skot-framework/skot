package tech.skot.model

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import tech.skot.core.SKLog

class TestWrapper {

    @Test
    fun trivial() {

        class ConteneurDeSKData(val name:String, val sk:SKData<String>)

        val metaSK = SKManualData<ConteneurDeSKData?>(null)

        val manual1 = ConteneurDeSKData(name = "manual1",SKManualData<String>("val1"))
        val manual2 = ConteneurDeSKData(name = "manual2",SKManualData<String>("val2"))
        runBlocking {
            val wrapped = wrap(
                stepData = metaSK,
                defaultValue = "Rien"
            ) {
                Log.d("SKLog","new conteneur de SK : ${this?.name}")
                this?.sk
            }

            delay(500)
            Log.d("SKLog","1->" + wrapped.get())
            metaSK.value = manual1
            delay(500)
            Log.d("SKLog","2->" + wrapped.get())
            metaSK.value = manual2
            delay(500)
            Log.d("SKLog","3->" + wrapped.get())
            metaSK.value = null
            delay(500)
            Log.d("SKLog","4->" + wrapped.get())
            delay(500)
            wrapped.cancel()
        }


    }
}