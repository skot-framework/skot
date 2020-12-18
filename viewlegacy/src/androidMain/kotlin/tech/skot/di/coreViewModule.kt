package tech.skot.di

import tech.skot.core.components.*
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.view.legacy.StackViewProxy

val coreViewModule = module<BaseInjector> {
    single { RootStackViewProxy as RootStackView }
    factory { StackViewProxy() as StackView }
    factory { AlertViewProxy() as AlertView }
    factory { SnackBarViewProxy() as SnackBarView }
}