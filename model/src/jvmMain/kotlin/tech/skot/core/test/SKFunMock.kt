package tech.skot.core.test

import kotlin.test.assertTrue

open class SKFunMock<C : Any, R : Any?>(val name: String) {

    var calls: MutableList<C> = mutableListOf()

    var returns: List<R> = emptyList()

    fun willReturn(r: R) {
        returns = listOf(r)
    }

    fun willReturnList(rs: List<R>) {
        returns = rs
    }

    var nextCallException: Exception? = null
    fun willFail(ex: Exception) {
        nextCallException = ex
    }

    operator fun invoke(c: C): R {
        calls.add(c)
        nextCallException?.let {
            nextCallException = null
            throw it
        }
        return getReturn()
    }

    fun assertCalled(rule:String = "") {
        assertTrue("$rule -> $name : la méthode doit avoir été appelé") {
            calls.isNotEmpty()
        }
    }

    open fun getReturn(): R {
        return when (returns.size) {
            0 -> throw Exception("You have to set returns of $name before function be called")
            1 -> returns.first()
            else -> {
                returns.first().also {
                    returns = returns.drop(1)
                }
            }
        }
    }
}

class SKFunUnitMock<C : Any>(name: String) : SKFunMock<C, Unit>(name) {
    override fun getReturn() {
        return Unit
    }
}