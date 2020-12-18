package tech.skot.core.components

interface ComponentView {
    fun onRemove()
}

interface ScreenView: ComponentView