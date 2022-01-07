package tech.skot.core.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import tech.skot.core.SKLog
import tech.skot.core.components.SKComponent
import tech.skot.core.di.InjectorMock
import tech.skot.core.di.Module
import tech.skot.core.di.injector
import kotlin.test.fail

abstract class SKTestViewModel(vararg modules: Module<InjectorMock>) {

    private val _modules: List<Module<InjectorMock>> = modules.asList()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    class ErrorTreated(val comp: SKComponent<*>, val ex: Exception, var errorMessage: String?)

    val errorsTreated = mutableListOf<ErrorTreated>()


    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
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



    fun CoroutineScope.step(
        model: (() -> Unit)? = null,
        user: (() -> Unit)? = null,
        test: suspend CoroutineScope.() -> Unit,
        then: (CoroutineScope.() -> Unit)? = null
    ) {
        launch {
            model?.invoke()
            user?.invoke()
            launch {
                test.invoke(this)
            }
            then?.invoke(this)
        }
    }

}

inline fun <reified C:Any>Any.assertAs():C = (this as? C) ?: fail("not a ${C::class.simpleName} (but ${this::class.simpleName})")