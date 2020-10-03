package tech.skot.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.model.IosPersistor
import tech.skot.model.PersistorFactory

actual val modelFrameworkModule = module<BaseInjector> {
    factory {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String) =
                    IosPersistor(dbFileName)
        } as PersistorFactory
    }
}