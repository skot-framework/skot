package tech.skot.core.di

import tech.skot.core.di.CoreViewInjectorImpl
import tech.skot.core.di.coreViewModule

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}