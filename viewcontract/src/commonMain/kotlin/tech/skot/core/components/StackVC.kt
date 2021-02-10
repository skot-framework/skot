package tech.skot.core.components

@IdLayout
interface StackVC : ComponentVC {
    var screens: List<ScreenVC>
}