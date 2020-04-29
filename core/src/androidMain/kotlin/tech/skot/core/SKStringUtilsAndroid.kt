package tech.skot.core

actual fun String.skFormat(vararg values: Any?): String = this.format(*values)