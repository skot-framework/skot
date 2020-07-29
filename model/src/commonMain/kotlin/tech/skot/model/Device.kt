package tech.skot.model

interface Device {
    class Locale(val language:String, val country:String)
    fun getLocaleInfos():Locale
    val osVersion:String
}
