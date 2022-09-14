package tech.skot.model

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class SKDataWrapper<D : Any?>(
    private val defaultValue: D,
    private val getSKData: suspend () -> SKData<D>?,
    private val newSKDataFlow: Flow<*>,
    private val scope: CoroutineScope,
) : SKData<D> {

    override val flow =
        MutableStateFlow<DatedData<D>>(DatedData(defaultValue, currentTimeMillis()))
    override val defaultValidity
        get() = _currentWrappedSKData?.defaultValidity ?: Long.MAX_VALUE

    override val _current: DatedData<D>
        get() = flow.value


    private var _currentWrappedSKData: SKData<D>? = null
    private var currentCollectJob: Job? = null

    private suspend fun subscribeToWrapped(throwError: Boolean) {
        getSKData().let { newSKToBeWrapped ->
            _currentWrappedSKData = newSKToBeWrapped
            currentCollectJob?.cancel()

            if (newSKToBeWrapped != null) {
                val errorToThrow: Exception? = try {
                    newSKToBeWrapped.get(validity = Long.MAX_VALUE)
                    flow.value = newSKToBeWrapped._current ?: DatedData(defaultValue)
                    null
                } catch (ex: Exception) {
                    if (ex !is CancellationException) {
                        flow.value = DatedData(defaultValue)
                        ex
                    } else {
                        null
                    }
                } finally {
                    currentCollectJob = scope.launch {
                        newSKToBeWrapped.flow.collect {
                            it?.let {
                                flow.value = DatedData(it.data, it.timestamp)
                            }
                        }
                    }
                }
                if (throwError && errorToThrow != null) {
                    throw errorToThrow
                }

            } else {
                flow.value = DatedData(defaultValue)
            }
        }
    }

    private var wrappedSkChangedJob: Job? = null

    private var launchMutex = Mutex()
    private suspend fun launch() {

        launchMutex.withLock {
            if (wrappedSkChangedJob == null) {
                subscribeToWrapped(true)
                wrappedSkChangedJob = scope.launch {
                    newSKDataFlow.drop(1).collect {
                        subscribeToWrapped(false)
                    }
                }
            }
        }
    }


    //pour arrêter manuellement le wrapper, pricuipalement utilisé pour les tests, si on ne maîtrise pas le scope
    fun cancel() {
        wrappedSkChangedJob?.cancel()
        currentCollectJob?.cancel()
    }

    override suspend fun update(): D {
        if (wrappedSkChangedJob == null) {
            launch()
        }
        return _currentWrappedSKData?.update() ?: defaultValue
    }

    override suspend fun get(validity: Long?): D {
        if (wrappedSkChangedJob == null) {
            launch()
        }
        return super.get(validity)
    }

    override suspend fun fallBackValue(): D? {
        return _current.data
    }
}


fun <D : Any?, R : Any?> CoroutineScope.wrap(
    stepData: SKData<D>,
    defaultValue: R,
    targetSKData: suspend D.() -> SKData<R>?,
): SKDataWrapper<R> {
    return SKDataWrapper<R>(
        defaultValue = defaultValue,
        getSKData = {
            stepData.get()?.let { targetSKData(it) }
        },
        newSKDataFlow = stepData.flow,
        scope = this
    )
}


fun <D : Any?, R : Any?> SKData<D>.wrap(
    scope: CoroutineScope,
    defaultValue: R,
    targetSKData: suspend D.() -> SKData<R>?,
): SKDataWrapper<R> {
    return scope.wrap(this, defaultValue, targetSKData)
}

