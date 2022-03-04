package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsRoot
import tech.skot.core.view.Color
import tech.skot.core.view.Icon
import tech.skot.core.view.Resource

@SKLayoutIsRoot
interface SKSnackBarVC : SKComponentVC {

    data class Shown(
        val message: String,
        val action: Action? = null,
        val position: Position = Position.TopWithInsetMargin,
        val background: Resource? = null,
        val textColor: Color? = null,
        val leftIcon: Icon? = null,
        val rightIcon: Icon? = null,
        val infiniteLines: Boolean = false,
    )

    data class Action(val label: String, val action: () -> Unit)

    sealed class Position {
        object Bottom : Position()
        object TopWithInsetMargin : Position()
        object TopWithInsetPadding : Position()
        class TopWithCustomMargin(val margin: Int) : Position()
        class BottomWithCustomMargin(val margin: Int) : Position()
    }


    var state: Shown?

}