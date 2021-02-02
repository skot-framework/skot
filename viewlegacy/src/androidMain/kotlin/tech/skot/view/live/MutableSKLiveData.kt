package tech.skot.view.live

import kotlin.reflect.KProperty

open class MutableSKLiveData<D>(initialValue: D) : SKLiveData<D>(initialValue) {
    fun postValue(newVal: D) {
        this.value = newVal
    }

    operator fun setValue(thisObj: Any?, property: KProperty<*>, value: D) {
        this.value = value
    }

    operator fun getValue(thisObj: Any?, property: KProperty<*>) = this.value
}
