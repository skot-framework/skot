package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

object RootStack:Stack() {
    override val view = coreViewInjector.rootStack()
}