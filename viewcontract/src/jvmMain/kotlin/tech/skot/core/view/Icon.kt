package tech.skot.core.view

actual open class Icon(val res:Int) : Resource {
    override fun equals(other: Any?): Boolean {
        return other is Icon && other.res == res
    }
}