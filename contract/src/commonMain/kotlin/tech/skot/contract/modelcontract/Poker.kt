package tech.skot.contract.modelcontract

class Poker {

    var observers: MutableSet<() -> Unit> = mutableSetOf()

    fun poke() {
        observers.forEach { it.invoke() }
    }

    fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

}
