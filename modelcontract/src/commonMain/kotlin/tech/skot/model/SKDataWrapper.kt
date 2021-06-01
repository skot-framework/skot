package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SKDataWrapper<D : Any>(
    private val defaultValue: D?,
    private val getSKData: suspend () -> SKData<D>?,
    newSKDataFlow: Flow<*>,
    private val scope: CoroutineScope
) : SKData<D?> {

    override val flow =
        MutableStateFlow<DatedData<D?>>(DatedData(defaultValue, currentTimeMillis()))
    override val defaultValidity = Long.MAX_VALUE
    override val _current: DatedData<D?>
        get() = flow.value


    private var _currentWrappedSKData: SKData<D>? = null
    private var currentCollectJob: Job? = null

    private suspend fun subscribeToWrapped() {
        getSKData().let { newSKToBeWrapped ->
            _currentWrappedSKData = newSKToBeWrapped
            currentCollectJob?.cancel()

            if (newSKToBeWrapped != null) {
                currentCollectJob = scope.launch {
                    newSKToBeWrapped.flow.collect {
                        it?.let {
                            flow.value = DatedData(it.data, it.timestamp)
                        }
                    }
                }
            } else {
                flow.value = DatedData(defaultValue)
            }
        }
    }

    private val wrappedSkChangedJob: Job? = scope.launch {
        newSKDataFlow.collect {
            subscribeToWrapped()
        }
    }

    init {
        scope.launch {
            subscribeToWrapped()
        }
    }

//    //pour arrêter manuellement le wrapper, pricuipalement utilisé pour les tests, si on ne maîtrise pas le scope
//    fun cancel() {
//
//    }

    override suspend fun update(): D? {
        return _currentWrappedSKData?.update() ?: _current.data
    }

    override suspend fun fallBackValue(): D? {
        return _current.data
    }

}


fun <D : Any, R : Any> CoroutineScope.wrap(
    stepData: SKData<D?>,
    defaultValue: R? = null,
    targetSKData: D.() -> SKData<R>?
): SKData<R?> {
    return SKDataWrapper(
        defaultValue = defaultValue,
        getSKData = {
            stepData.get()?.let { targetSKData(it) }
        },
        newSKDataFlow = stepData.flow,
        scope = this
    )
}


fun <D : Any, R : Any> SKData<D?>.wrap(
    scope: CoroutineScope,
    defaultValue: R? = null,
    targetSKData: D.() -> SKData<R>?
): SKData<R?> {
    return scope.wrap(this, defaultValue, targetSKData)
}

