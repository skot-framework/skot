package tech.skot.di

import tech.skot.CoreViewInjector
import tech.skot.components.CoreViewInjectorImpl
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module

val viewFrameworkModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}