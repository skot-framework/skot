package tech.skot.di

import tech.skot.CoreViewInjector
import tech.skot.components.CoreViewInjectorImpl
import tech.skot.core.di.module

val viewFrameworkModule = module {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}