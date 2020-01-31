package tech.skot.model

data class DatedData<D : Any>(val data: D, val timestamp: Long)

