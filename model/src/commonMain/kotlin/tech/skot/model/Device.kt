package tech.skot.model

import tech.skot.core.di.get

interface Device {
    class Locale(val language:String, val country:String)
    fun getLocaleInfos():Locale
}

val device by lazy {
    get<Device>()
}