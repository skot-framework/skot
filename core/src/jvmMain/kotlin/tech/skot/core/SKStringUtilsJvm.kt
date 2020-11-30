package tech.skot.core

actual fun String.skFormat(vararg values: String): String = this.format(*values)