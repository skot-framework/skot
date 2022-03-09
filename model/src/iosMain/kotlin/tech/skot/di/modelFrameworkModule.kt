package tech.skot.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.model.IosSKPersistor
import tech.skot.model.PersistorFactory
import tech.skot.model.SKPersistor

actual val modelFrameworkModule = module<BaseInjector> {
    factory {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String, cache: Boolean): SKPersistor =
                IosSKPersistor(dbFileName)
        } as PersistorFactory
    }
}