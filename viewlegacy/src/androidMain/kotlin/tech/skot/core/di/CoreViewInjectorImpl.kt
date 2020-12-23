package tech.skot.core.di

import tech.skot.core.components.RootStackViewProxy
import tech.skot.core.components.presented.AlertViewProxy
import tech.skot.core.components.presented.BottomSheetView
import tech.skot.core.components.presented.BottomSheetViewProxy
import tech.skot.core.components.presented.SnackBarViewProxy
import tech.skot.view.legacy.StackViewProxy


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack(bottomSheetView: BottomSheetView) = RootStackViewProxy(bottomSheetView as BottomSheetViewProxy)

    override fun stack() = StackViewProxy()

    override fun alert() = AlertViewProxy()

    override fun snackBar() = SnackBarViewProxy()

    override fun bottomSheet() = BottomSheetViewProxy()

}