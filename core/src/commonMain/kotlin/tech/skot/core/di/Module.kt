package tech.skot.core.di

import kotlin.reflect.KClass

abstract class Definition<D>


class Single<C,D>(val factory: C.() -> D) : Definition<D>()
class Factory<C,D>(val factory: C.() -> D) : Definition<D>()


class Module<C:Any> {

    //Les injections se font toujours dans le mainThread (onCreate des vues) donc on peut utiliser des mutableMap normales
    val singles = mutableMapOf<KClass<*>, Single<C,*>>()
    val factories = mutableMapOf<KClass<*>, Factory<C,*>>()

    inline fun <reified D> single(noinline def: C.() -> D) {
        singles[D::class] = Single(def)
    }

    inline fun <reified D> factory(noinline def: C.() -> D) {
        factories[D::class] = Factory(def)
    }


}

fun <C:Any>module(modDef:Module<C>.()->Unit):Module<C> {
    val mod = Module<C>()
    modDef(mod)
    return mod
}
