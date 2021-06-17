package tech.skot.core.components

import tech.skot.core.view.SKTransition

/**
 * Composant gérant une pile d'écrans,
 * les de vue ne sont pas gardés en mémoire, il faut mémoriser et restaurer les état
 * (scroll par exemple) si besoin
 */
@SKLayoutIsSimpleView
interface SKStackVC : SKComponentVC {
    open class State(open val screens:List<out SKScreenVC>, open val transition:SKTransition? = null)
    var state:State
}
