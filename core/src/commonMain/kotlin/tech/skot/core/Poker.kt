package tech.skot.core

open class Poker {

    protected var observers: MutableSet<() -> Unit> = mutableSetOf()

    fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

}

class MutablePoker : Poker() {
    fun poke() {
        observers.forEach { it.invoke() }
    }
}