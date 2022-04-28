package tech.skot.model

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.junit.Test
import kotlin.test.assertEquals

class TestPaginatedDataWithCache {

    val globalCache = JvmSKPersistor("global")

    val paginated = object:SKPaginatedDataWithCache<Int>(
        name ="TEST_PAGINATED",
        serializer = Int.serializer(),
        key = null,
        cache = globalCache,
        validity = null,
        fetchPage = { index, nbItemsInPage ->
            ((index*nbItemsInPage)..((index+1)*nbItemsInPage-1)).map { it }
        },
        nbItemsInPage = 8
    ) {}

    suspend fun assertPaginatedValuesEquals(expected:List<Int>, message:String? = null) {
        assertEquals(
            expected = expected,
            actual = paginated.get(),
            message = message
        )
    }

    suspend fun assertPaginatedValuesEquals(expected:IntRange, message:String? = null) {
        assertPaginatedValuesEquals(expected.toList(), message)
    }


    @Test
    fun testloadPages() {
        runBlocking {
            paginated.update()
            assertPaginatedValuesEquals(0..7)
            paginated.oneMorePage()
            assertPaginatedValuesEquals(0..15)
            paginated.oneMorePage()
            assertPaginatedValuesEquals(0..23)
        }


    }

    @Test
    fun testInvalidate() {
        runBlocking {
            paginated.update()
            assertPaginatedValuesEquals(0..7)
            paginated.invalidateFrom(9)
            assertPaginatedValuesEquals(0..7)
            paginated.invalidateFrom(4)
            assertPaginatedValuesEquals(emptyList())

            paginated.update()
            assertPaginatedValuesEquals(0..7)
            paginated.oneMorePage()
            paginated.oneMorePage()
            paginated.oneMorePage()
            paginated.oneMorePage()
            paginated.oneMorePage()
            assertPaginatedValuesEquals(0..47)
            paginated.invalidateFrom(23)
            assertPaginatedValuesEquals(0..15)
        }

    }


    @Test
    fun testReplace() {
        runBlocking {
            paginated.update()
            paginated.oneMorePage()
            assertPaginatedValuesEquals(0..15)

            paginated.replace(4, 234)
            assertPaginatedValuesEquals(listOf(
                0,1,2,3,234,5,6,7,8,9,10,11,12,13,14,15
            ))
            paginated.replace(13, 0)
            assertPaginatedValuesEquals(listOf(
                0,1,2,3,234,5,6,7,8,9,10,11,12,0,14,15
            ))
        }

    }
}