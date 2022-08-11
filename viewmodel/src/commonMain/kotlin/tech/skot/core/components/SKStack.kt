package tech.skot.core.components

import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.SKTransition

open class SKStack : SKComponent<SKStackVC>() {
    override val view = coreViewInjector.stack()

    class State(val screens: List<SKScreen<*>>, val transition: SKTransition? = null)

    var defaultPushTransition: SKTransition? = null
    var defaultPopTransition: SKTransition? = null

    var state: State = State(emptyList(), null)
        set(value) {
            val transition = value.transition ?: when {
                defaultPushTransition != null && value.screens.dropLast(1) == field.screens -> {
                    defaultPushTransition
                }
                defaultPopTransition != null && field.screens.dropLast(1) == value.screens -> {
                    defaultPopTransition
                }
                else -> {
                    null
                }
            }
            view.state = SKStackVC.State(
                screens = value.screens.map { it.view },
                transition = transition
            )
            field.screens.forEach { if (!value.screens.contains(it)) it.onRemove() }
            value.screens.forEach { it.parent = this }
            field = value
        }

    var content: SKScreen<*>
        get() = state.screens.last()
        set(value) {
//            SKLog.d("Stack will set screens to $value")
            state = State(listOf(value))
        }

    fun push(screen: SKScreen<*>, transition: SKTransition? = null) {
//        SKLog.d("Will push screen: ${screen::class.simpleName}")
        state = State(state.screens + screen, transition)
    }

    fun replace(oldScreen: SKScreen<*>, newScreen: SKScreen<*>, transition: SKTransition? = null) {
        state = State(state.screens.map {
            if (it == oldScreen) newScreen else it
        }, transition)
    }

    fun pop(transition: SKTransition? = null, ifRoot: (() -> Unit)? = null) {
        if (state.screens.size > 1) {
            state = State(state.screens - state.screens.last(), transition)
        } else {
            ifRoot?.invoke()
        }

    }

    fun remove(screen: SKScreen<*>, transition: SKTransition? = null) {
        if (state.screens.contains(screen)) {
            state = State(screens = state.screens - screen, transition = transition)
        }
    }

    override fun onRemove() {
        super.onRemove()
        state.screens.forEach { it.onRemove() }
    }

//    val screenOnTop: SKScreen<*>?
//        get() = state.screens.lastOrNull()

}

