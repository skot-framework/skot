package tech.skot.core.di

import tech.skot.core.components.RootStackView
import tech.skot.core.components.StackView
import tech.skot.core.components.presented.AlertView
import tech.skot.core.components.presented.BottomSheetView
import tech.skot.core.components.presented.SnackBarView

interface CoreViewInjector {
    fun rootStack(): RootStackView
    fun stack(): StackView
    fun alert(): AlertView
    fun snackBar(): SnackBarView
    fun bottomSheet(): BottomSheetView
}

fun coreViewModule(coreViewInjector: CoreViewInjector) =
        module<BaseInjector> {
            single { coreViewInjector.rootStack() as RootStackView }
            factory { coreViewInjector.stack() as StackView }
            factory { coreViewInjector.alert() as AlertView }
            factory { coreViewInjector.snackBar() as SnackBarView }
            factory { coreViewInjector.bottomSheet() as BottomSheetView }
        }
