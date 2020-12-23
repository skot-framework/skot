package tech.skot.core.components

interface ScreenView: ComponentView {
    var onBackPressed:(()->Unit)?
}