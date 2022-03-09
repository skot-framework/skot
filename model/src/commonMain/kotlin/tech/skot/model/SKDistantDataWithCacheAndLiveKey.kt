package tech.skot.model

import kotlinx.serialization.KSerializer

abstract class SKDistantDataWithCacheAndLiveKey<D : Any>(
    name: String,
    serializer: KSerializer<D>,
    private val cache: SKPersistor = globalPersistor,
    validity: Long? = null,
    private val fixKey: String?,
    private val liveKey: () -> String,
    fetchData: suspend () -> D
) : SKDistantDataWithCache<D>(
    name = name,
    serializer = serializer,
    key = "${fixKey}_${liveKey()}",
    cache = cache,
    validity = validity,
    fetchData = fetchData
) {

    override suspend fun get(validity: Long?): D {
        if (_current != null && liveKeyOfCurrentValue != liveKey()) {
            update()
        }
        return super.get(validity)
    }

    override val completeKey: String?
        get() = "${fixKey}_${liveKey()}"

    private var liveKeyOfCurrentValue: String? = null
    override fun setDatedData(newDatedData: DatedData<D>) {
        liveKeyOfCurrentValue = liveKey()
        super.setDatedData(newDatedData)
    }

    override suspend fun newDatedData(): DatedData<D> {
        return super.newDatedData().also { liveKeyOfCurrentValue = liveKey() }
    }

    override suspend fun fallBackValue(): D? {
        if (liveKeyOfCurrentValue == liveKey()) {
            return super.fallBackValue()
        } else {
            return null
        }
    }
}