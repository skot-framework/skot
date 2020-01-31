package tech.skot.di

import tech.skot.core.di.module
import tech.skot.model.*

actual val modelFrameworkModule = module {
    factory {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String) =
                    AndroidPersistor(androidApplication, dbFileName)
        } as PersistorFactory
    }

    factory {
        AndroidPrefs(androidApplication) as Prefs
    }

    factory {
        AndroidDevice() as Device
    }
}