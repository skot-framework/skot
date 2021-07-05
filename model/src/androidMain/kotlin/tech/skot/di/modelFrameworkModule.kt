package tech.skot.di

import android.content.Context
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.model.*

actual val modelFrameworkModule = module<BaseInjector> {

    single {
        androidApplication as Context
    }

    factory {
        object : PersistorFactory {
            override fun getPersistor(dbFileName: String) =
                    AndroidSKPersistor(androidApplication, dbFileName)
        } as PersistorFactory
    }

    factory {
        AndroidPrefs(androidApplication) as Prefs
    }

    factory {
        AndroidDevice() as Device
    }
}