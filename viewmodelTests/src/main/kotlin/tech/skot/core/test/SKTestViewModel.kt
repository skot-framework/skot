package tech.skot.core.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import tech.skot.core.SKLog
import tech.skot.core.components.*
import tech.skot.core.di.InjectorMock
import tech.skot.core.di.Module
import tech.skot.core.di.injector
import kotlin.test.fail

abstract class SKTestViewModel(vararg modules: Module<InjectorMock>) {

    private val _modules: List<Module<InjectorMock>> = modules.asList()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    data class ErrorTreated(val comp: SKComponent<*>, val ex: Exception, var errorMessage: String?)

    val errorsTreated = mutableListOf<ErrorTreated>()

    val scheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scheduler))
        injector = InjectorMock(_modules)
        SKComponent.errorTreatment = { comp, ex, errorMessage ->
            errorsTreated.add(ErrorTreated(comp = comp, ex = ex, errorMessage = errorMessage))
            SKLog.e(ex, errorMessage)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    fun SKScreen<*>.isRemoved(): Boolean = (view as SKScreenViewMock).removed
    fun SKScreenVC.isRemoved(): Boolean = (this as SKScreenViewMock).removed


    val screenOnTop: SKScreen<*>?
        get() = SKRootStack.state.screens.lastOrNull()

    suspend fun step(
        model: (() -> Unit)? = null,
        user: (() -> Unit)? = null,
        withAdvanceUntilIdle: Boolean = true,
        test: (suspend () -> Unit)? = null,
    ) {
        if (withAdvanceUntilIdle) scheduler.advanceUntilIdle()
        model?.invoke()
        if (withAdvanceUntilIdle) scheduler.advanceUntilIdle()
        user?.invoke()
        if (withAdvanceUntilIdle) scheduler.advanceUntilIdle()
        test?.invoke()
    }

}

inline fun <reified C : Any> Any.assertAs(): C =
    (this as? C) ?: fail("not a ${C::class.simpleName} (but ${this::class.simpleName})")