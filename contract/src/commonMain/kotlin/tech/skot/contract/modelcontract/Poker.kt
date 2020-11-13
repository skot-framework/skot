package tech.skot.contract.modelcontract

open class Poker {

    protected var observers: MutableSet<() -> Unit> = mutableSetOf()

    fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

}

open class MutablePoker : Poker() {
    fun poke() {
        observers.forEach { it.invoke() }
    }
}

class PokerWrapper:MutablePoker() {

    private val actualPokerObserver = { poke() }

    var actualPoker:Poker? = null
        set(value) {
            field?.removeObserver(actualPokerObserver)
            field = value
            value?.addObserver(actualPokerObserver)
        }


}