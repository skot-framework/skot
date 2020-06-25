package tech.skot.components

/**
 * Attention cette classe ne remove aucun screen, le cycle de vie des screens ajoutés doit être géré extérieurement
 *
 */
abstract class FrameKeepingScreens<V : FrameKeepingScreensView> : Component<V>() {

    var screen: Screen<out ScreenView>? = null
        set(value) {
            field = value
            view.screen = value?.view
        }


}