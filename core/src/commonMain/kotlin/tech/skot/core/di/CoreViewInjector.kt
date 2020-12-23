package tech.skot.core.di

import tech.skot.core.components.RootStack
import tech.skot.core.components.RootStackView
import tech.skot.core.components.StackView
import tech.skot.core.components.presented.AlertView
import tech.skot.core.components.presented.BottomSheetView
import tech.skot.core.components.presented.SnackBarView

interface CoreViewInjector {
    fun rootStack(bottomSheetView: BottomSheetView): RootStackView
    fun stack(): StackView
    fun alert(): AlertView
    fun snackBar(): SnackBarView
    fun bottomSheet(): BottomSheetView
}

val coreViewInjector: CoreViewInjector by lazy {
    get<CoreViewInjector>()
}

val rootStack by lazy { RootStack() }