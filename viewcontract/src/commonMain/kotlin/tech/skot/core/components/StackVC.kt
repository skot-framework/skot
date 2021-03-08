package tech.skot.core.components

@SKLayoutIsSimpleView
interface StackVC : ComponentVC {
    var screens: List<ScreenVC>
}