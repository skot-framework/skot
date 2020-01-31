package tech.skot.model

data class DatedData<D : Any>(val data: D, val id:String?, val timestamp: Long)

