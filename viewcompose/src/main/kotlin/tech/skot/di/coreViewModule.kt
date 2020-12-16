package tech.skot.di

import tech.skot.contract.view.RootStackView
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.view.compose.RootStackViewImpl

val coreViewModule = module<BaseInjector> {
    single { RootStackViewImpl() as RootStackView }
}