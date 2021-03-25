package tech.skot.core.components

/**
 * Composant gérant une pile d'écrans,
 * les de vue ne sont pas gardés en mémoire, il faut mémoriser et restaurer les état
 * (scroll par exemple) si besoin
 */
@SKLayoutIsSimpleView
interface StackVC : ComponentVC {
    var screens: List<ScreenVC>
}