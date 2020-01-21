package tech.skot.di

import tech.skot.core.di.module
import tech.skot.model.AndroidPersistor
import tech.skot.model.PersistorFactory

actual val modelFrameworkModule = module {
    factory {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String) =
                    AndroidPersistor(androidApplication, dbFileName)
        } as PersistorFactory
    }
}