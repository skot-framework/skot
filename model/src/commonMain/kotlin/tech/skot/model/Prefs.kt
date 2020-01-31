package tech.skot.model

import tech.skot.core.di.get

interface Prefs {
    fun getString(key:String):String?
    fun putString(key:String, value:String?)

    fun getInt(key:String):Int?
    fun putInt(key:String, value:Int?)

    fun getLong(key:String):Long?
    fun putLong(key:String, value:Long?)


    fun remove(key:String)
}

val prefs by lazy {
    get<Prefs>()
}