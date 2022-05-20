package tech.skot.model

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.skot.core.currentTimeMillis

interface SKLruCache<K : Any, D : Any?> {
    suspend fun get(key: K): D
    suspend fun clear()
}

open class SKLruMemoryCache<K : Any, D : Any?>(size: Int, private val dataValidity: Long? = null, private val getFresh: suspend (key: K) -> D) : SKLruCache<K, D> {

    inner class Data(val key: K, var dataDate: Long, var lastAccess: Long, var data: D) {
        override fun toString(): String {
            return "$key -> $data"
        }
    }

    private val datas: Array<Data?> = arrayOfNulls<Data?>(size)//List(size) { null }


    private val mutex = Mutex()
    override suspend fun get(key: K): D {
        println(datas.map { it.toString() }.joinToString(","))
        mutex.withLock {
            val now = currentTimeMillis()
            val currentDataForThisKey = datas.find { it?.key == key }
            //on a déjà un slot pour cette clé
            if (currentDataForThisKey != null) {
                currentDataForThisKey.lastAccess = now
                if (dataValidity != null && (currentDataForThisKey.dataDate + dataValidity) < now) {
                    //La donnée n'est plus valable, on doit la réactualiser
                    currentDataForThisKey.data = getFresh(key)
                    currentDataForThisKey.dataDate = now
                }
                return currentDataForThisKey.data
            } else {
                val newData = Data(
                        key = key,
                        dataDate = now,
                        lastAccess = now,
                        data = getFresh(key)
                )
                datas.sortBy { it?.lastAccess ?: 0 }
                datas[0] = newData
                return newData.data
            }
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            datas.fill(null)
        }
    }


}