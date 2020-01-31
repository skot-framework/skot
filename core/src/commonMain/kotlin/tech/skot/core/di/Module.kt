package tech.skot.core.di

import kotlin.reflect.KClass

abstract class Definition<D>

class InjectionContext(val injector: Injector) {
    inline fun <reified D : Any> get()
            : D {
        return injector.get(D::class)
    }

}

class Single<D>(val factory: Injector.() -> D) : Definition<D>()
class Factory<D>(val factory: Injector.() -> D) : Definition<D>()


class Module {

    //Les injections se font toujours dans le mainThread (onCreate des vues) donc a priori on peut utiliser des mutableMap normales
    val singles = mutableMapOf<KClass<*>, Single<*>>()
    val factories = mutableMapOf<KClass<*>, Factory<*>>()

    inline fun <reified D> single(noinline def: Injector.() -> D) {
        singles[D::class] = Single(def)
    }

    inline fun <reified D> factory(noinline def: Injector.() -> D) {
        factories[D::class] = Factory(def)
    }


}

fun module(modDef:Module.()->Unit):Module {
    val mod = Module()
    modDef(mod)
    return mod
}
