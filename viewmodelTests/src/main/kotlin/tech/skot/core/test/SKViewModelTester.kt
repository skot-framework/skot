package tech.skot.core.test

import tech.skot.core.components.SKComponent
import tech.skot.core.components.SKComponentViewMock
import tech.skot.core.components.SKScreen
import kotlin.reflect.KProperty1

abstract class SKViewModelTester<V : SKComponentViewMock, M>(val component: SKComponent<*>) {
    val view: V = component.view as V
    val model: M by lazy {
        (component::class.members.first { it.name == "model" } as KProperty1<Any, *>)
            .get(component) as M
    }
    val screen:SKScreen<*> by lazy {
        component as SKScreen<*>
    }
}