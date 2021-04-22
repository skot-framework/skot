package tech.skot.core.components

/**
 * Composant affichant un écran parmis une sélection, gardée en mémoire
 */
@SKLayoutIsSimpleView
interface SKFrameVC:SKComponentVC {

    val screens:Set<SKScreenVC>

    //L'écran actuellement affiché, doit être un des écrans de screens
    var screen:SKScreenVC?

}