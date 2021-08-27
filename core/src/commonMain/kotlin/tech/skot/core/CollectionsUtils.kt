package tech.skot.core

public inline fun <T, R> Iterable<T>.mapWithLast(transform: (data:T, isLast:Boolean) -> R): List<R> {
    val last = this.lastOrNull()
    return map { transform(it, it == last) }
}