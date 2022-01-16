package tech.skot.model.test

import org.junit.Before
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.Module
import tech.skot.core.di.injector

abstract class SKTestModel(vararg modules: Module<BaseInjector>) {

    private val _modules: List<Module<BaseInjector>> = modules.asList()

    @Before
    fun setUp() {
        injector = BaseInjector(_modules)
    }


}