package tech.skot.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.model.Device
import tech.skot.model.JvmSKPersistor
import tech.skot.model.PersistorFactory

val mockDevice = object : Device {
    private var localeInfos: Device.Locale = Device.Locale(language = "FR", country = "FR")
    override fun getLocaleInfos(): Device.Locale = localeInfos

    override var osVersion: String = "30"
}

actual val modelFrameworkModule = module<BaseInjector> {
    factory<PersistorFactory> {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String) =
                JvmSKPersistor(dbFileName)
        }
    }

    factory<Device> { mockDevice }
}