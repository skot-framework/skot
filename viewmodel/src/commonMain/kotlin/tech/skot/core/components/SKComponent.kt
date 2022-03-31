package tech.skot.core.components

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import tech.skot.core.Poker
import tech.skot.core.SKLog
import tech.skot.core.view.SKPermission
import tech.skot.model.SKData
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class SKComponent<out V : SKComponentVC> : CoroutineScope {
    abstract val view: V

    companion object {
        var errorTreatment: ((component: SKComponent<*>, exception: Exception, errorMessage: String?) -> Unit)? =
            null
    }


    protected val job = SupervisorJob()
    final override val coroutineContext = CoroutineScope(Dispatchers.Main + job).coroutineContext

    open fun onRemove() {
        //remove des Pokers
        removeObservers.forEach { it.invoke() }
        view.onRemove()
        job.cancel()
    }


    private val noCrashExceptionHandler = CoroutineExceptionHandler { _, e ->
        SKLog.i("launchNoCrash ${e.message}")
    }

    fun launchNoCrash(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job =
        launch(noCrashExceptionHandler, start, block)


    open val loader: SKLoader? = null

    open val onSwipe: (() -> Unit)? = null

    open fun treatError(exception: Exception, errorMessage: String?) {
        errorTreatment?.invoke(this, exception, errorMessage)
            ?: throw IllegalStateException("Valorise SKComponent.errorTreatment or override treatError function to use method treating errors")
    }



    fun requestPermissions(permissions: List<SKPermission>, onResult:(grantedPermissions:List<SKPermission>)->Unit) {
        view.requestPermissions(permissions = permissions, onResult = onResult)
    }

    fun doWithPermission(permission:SKPermission, onKo:(()->Unit)? = null, onOk:()->Unit) {
        requestPermissions(listOf(permission)) { grantedPermissions->
            if (grantedPermissions.isNotEmpty()) {
                onOk()
            }
            else {
                onKo?.invoke()
            }
        }
    }

    fun doWithPermissions(vararg permissions:SKPermission, onKo:(()->Unit)? = null, onOk:()->Unit) {
        val permissionsList = permissions.asList()
        requestPermissions(permissionsList) { grantedPermissions->
            if (grantedPermissions.containsAll(permissionsList)) {
                onOk()
            }
            else {
                onKo?.invoke()
            }
        }
    }

    fun hasPermission(vararg permission:SKPermission): Boolean {
        return view.hasPermission(*permission)
    }

    fun displayMessageDebug(message:String) {
        view.displayMessage(SKComponentVC.Message.Debug(message))
    }

    fun displayMessageInfo(message:String) {
        view.displayMessage(SKComponentVC.Message.Info(message))
    }

    fun displayMessageWarning(message:String) {
        view.displayMessage(SKComponentVC.Message.Warning(message))
    }

    fun displayMessageError(message:String) {
        view.displayMessage(SKComponentVC.Message.Error(message))
    }

    fun launchWithOptions(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        withLoader: Boolean = false,
        specificErrorTreatment: ((ex: Exception) -> Unit)? = null,
        errorMessage: String? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job =
        if (withLoader && loader == null) {
            throw IllegalStateException("You have to override loader property to launchWithLoader")
        } else {
            launch(context, start) {
                if (withLoader) {
                    loader?.workStarted()
                }
                try {
                    block()
                } catch (ex: Exception) {
                    if (ex !is CancellationException) {
                        specificErrorTreatment?.invoke(ex) ?: treatError(ex, errorMessage)
                    }
                } finally {
                    if (withLoader) {
                        loader?.workEnded()
                    }
                }

            }
        }

    fun launchWithLoaderAndErrors(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorMessage: String? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job =
        launchWithOptions(
            context = context,
            start = start,
            errorMessage = errorMessage,
            withLoader = true,
            block = block
        )


    private var removeObservers: MutableSet<() -> Unit> = mutableSetOf()
    private fun addRemoveObserver(observer: () -> Unit) {
        removeObservers.add(observer)
    }

    private fun removeRemoveObserver(observer: () -> Unit) {
        removeObservers.remove(observer)
    }

    fun observe(poker: Poker, onPoke: () -> Unit) {
        poker.addObserver(onPoke)
        addRemoveObserver { poker.removeObserver(onPoke) }
    }

    fun logD(message: Any?) {
        SKLog.d("${this::class.simpleName} -- $message")
    }

    fun logE(throwable: Throwable, message: Any? = "") {
        SKLog.e(throwable, "${this::class.simpleName} -- $message")
    }

    fun <D : Any?> SKData<D>.onChange(lambda: (d: D) -> Unit) {

        class Treatment(val data: D)

        var lastTreatedData: Treatment? = null

        launchNoCrash {
            flow.collect {
                it?.let {
                    it.data.let {
                        if (lastTreatedData == null) {
                            lastTreatedData = Treatment(it)
                        } else {
                            if (lastTreatedData?.data != it) {
                                lambda(it)
                                lastTreatedData = Treatment(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun <D : Any?> SKData<D>.onData(
        validity: Long? = null,
        withLoaderForFirstData: Boolean = true,
        fallBackDataBeforeFirstDataLoaded: Boolean = false,
        fallBackDataIfError: Boolean = false,
        treatErrors: Boolean = true,
        defaultErrorMessage: String? = null,
        block: (d: D) -> Unit
    ) {

        class Treatment(val data: D)

        var lastTreatedData: Treatment? = null

        fun treatData(data: D) {
            lastTreatedData.let { currentLast ->
                if (currentLast == null || data != currentLast.data) {
                    block(data)
                    lastTreatedData = Treatment(data)
                }
            }

        }

        fun fallBack(): Job =
            launchWithOptions(
                withLoader = withLoaderForFirstData,
                specificErrorTreatment = { ex ->
                    logE(ex, "SKData onData fallBackDataBeforeFirstDataLoaded error")
                }
            ) {
                fallBackValue()?.let { treatData(it) }
            }

        val fallBackJob: Job? =
            if (fallBackDataBeforeFirstDataLoaded) {
                fallBack()
            } else {
                null
            }
        launchWithOptions(
            withLoader = withLoaderForFirstData,
            specificErrorTreatment = { ex ->
                if (fallBackDataIfError) {
                    fallBack()
                }
                if (treatErrors) {
                    treatError(ex, defaultErrorMessage)
                }
            }

        ) {
            try {
                get(validity).let {
                    fallBackJob?.cancel()
                    treatData(it)
                }
            } catch (ex: Exception) {
                if (ex !is CancellationException) {
                    if (fallBackDataIfError) {
                        fallBack()
                    }
                    if (treatErrors) {
                        treatError(ex, defaultErrorMessage)
                    }
                }
            }
            launchNoCrash {
                flow.collect {
                    it?.let {
                        it.data.let { treatData(it) }
                    }
                }
            }
        }


    }

    open fun computeItemId(): Any {
        return this
    }

    operator fun plus(otherComponent: SKComponent<*>?): List<SKComponent<*>> =
        listOfNotNull(this, otherComponent)

    operator fun plus(otherComponents: List<SKComponent<*>>?): List<SKComponent<*>> =
        if (otherComponents != null) {
            listOf(this) + otherComponents
        } else listOf(this)


}

fun List<SKComponent<*>>.plusIfNotNull(otherComponent: SKComponent<*>?): List<SKComponent<*>> =
    if (otherComponent == null) {
        this
    } else {
        this + otherComponent
    }

fun List<SKComponent<*>>.join(separator: () -> SKComponent<*>): List<SKComponent<*>> {
    return if (size < 2) {
        this
    } else {
        val list = mutableListOf<SKComponent<*>>()
        val last = last()
        this.forEach {
            list.add(it)
            if (it != last) {
                list.add(separator())
            }
        }
        list
    }
}

fun List<List<SKComponent<*>>>.joinGroups(separator: () -> SKComponent<*>): List<SKComponent<*>> {
    val list = mutableListOf<SKComponent<*>>()
    val last = lastOrNull()
    this.forEach {
        list.addAll(it)
        if (it != last && it.isNotEmpty()) {
            list.add(separator())
        }
    }
    return list
}