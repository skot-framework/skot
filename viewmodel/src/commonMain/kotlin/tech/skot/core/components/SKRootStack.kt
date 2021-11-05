package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

val SKRootStack:SKStack by lazy {
    object : SKStack() {
        override val view = coreViewInjector.rootStack()
    }
}