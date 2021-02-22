package tech.skot.core.components

/**
 * Composant affichant un écran parmis une sélection, gardée en mémoire
 */
interface FrameVC:ComponentVC {

    val screens:Set<ScreenVC>

    //L'écran actuellement affiché, doit être un des écrans de screens
    var screen:ScreenVC?

}