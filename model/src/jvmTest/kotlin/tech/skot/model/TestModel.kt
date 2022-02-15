package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import tech.skot.core.SKLog
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.injector
import tech.skot.di.modelFrameworkModule

abstract class TestModel {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    val scheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scheduler))
        injector = BaseInjector(
            modules = listOf(
                modelFrameworkModule
            )
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


}