package tech.skot.components

import tech.skot.core.di.get


val coreViewInjector: CoreViewInjector by lazy {
    get<CoreViewInjector>()
}