package tech.skot.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import tech.skot.core.SKLog


abstract class SKPaginatedDataWithCache<D : Any>(
    name: String,
    serializer: KSerializer<D>,
    key: String?,
    cache: SKPersistor = globalPersistor,
    validity: Long? = null,
    private val nbItemsInPage: Int,
    private val fetchPage: suspend (index: Int, nbItemsInPage: Int) -> List<D>
) : SKDistantDataWithCache<List<D>>(
    name = name,
    serializer = ListSerializer(serializer),
    key = key,
    cache = cache,
    validity = validity,
    fetchData = {
        fetchPage(0, nbItemsInPage)
    }
), SKPaginatedData<D> {


    private var lastPageFetched: Boolean = false

    override suspend fun newDatedData(): DatedData<List<D>> {
        lastPageFetched = false
        return super.newDatedData()
    }

    override val mayHaveAnotherPage: Boolean
        get() = if (lastPageFetched) {
            false
        } else {
            val dataSize = _current?.data?.size
            dataSize == null || (dataSize != 0 && dataSize % nbItemsInPage == 0)
        }


    override suspend fun oneMorePage() {
        _current?.let { currentDatedData ->
            val newData = fetchPage(currentDatedData.data.size / nbItemsInPage, nbItemsInPage)
            currentDatedData.copy(data = currentDatedData.data + newData).let {
                setDatedData(it)
            }
            if (newData.isEmpty()) {
                lastPageFetched = true
            }
        }
    }


    fun invalidateFrom(data: D) {
        _current?.let { currentDatedData ->
            val indexOfFirstInvalidData = currentDatedData.data.indexOf(data)
            SKLog.d("indexOfFirstInvalidData $indexOfFirstInvalidData")
            if (indexOfFirstInvalidData != -1) {
                val indexOfFristValueToRemove = indexOfFirstInvalidData - indexOfFirstInvalidData%nbItemsInPage
                SKLog.d("indexOfFristValueToRemove $indexOfFristValueToRemove")
                setDatedData(currentDatedData.copy(data = currentDatedData.data.take(indexOfFristValueToRemove)))
            }
        }
    }

    fun replace(old: D, new: D) {
        _current?.let { currentDatedData ->
            setDatedData(currentDatedData.copy(data = currentDatedData.data.map {
                if (it == old) {
                    new
                } else {
                    it
                }
            }))
        }
    }
}