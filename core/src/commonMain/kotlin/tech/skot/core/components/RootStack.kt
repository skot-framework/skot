package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.components.presented.BottomSheet
import tech.skot.core.di.coreViewInjector

class RootStack : ScreenParent {

    init {
        SKLog.d("RootStack init !")
    }

    val bottomSheet = BottomSheet()
    val view = coreViewInjector.rootStack(bottomSheetView = bottomSheet.view)

    var screens: List<Screen<*>> = emptyList()
        set(value) {
            view.screens = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.parent = null }
            value.forEach { it.parent = this }
            field = value
        }

    var content: Screen<*>
        get() = screens.last()
        set(value) {
            screens = listOf(value)
        }

    override fun push(screen: Screen<*>) {
        screens = screens + screen
    }

    fun pop(ifRoot: (() -> Unit)? = null) {
        if (screens.size > 1) {
            screens = screens - screens.last()
        } else {
            ifRoot?.invoke()
        }
    }

    override fun remove(screen: Screen<*>) {
        if (screens.contains(screen)) {
            screens = screens - screen
        }
    }
}