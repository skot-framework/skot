package tech.skot.model.test

import org.junit.Before
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.Module
import tech.skot.core.di.injector
import tech.skot.model.globalCache
import tech.skot.model.userCache

abstract class SKTestModel(vararg modules: Module<BaseInjector>) {

    private val _modules: List<Module<BaseInjector>> = modules.asList()

    @Before
    fun setUp() {
        injector = BaseInjector(_modules)
        globalCache.clearSync()
        userCache.clearSync()
    }


}