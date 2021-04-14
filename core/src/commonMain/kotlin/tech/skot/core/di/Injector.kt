package tech.skot.core.di

import kotlin.reflect.KClass

/**
 * Pas d'injection par label pour l'instant, je considère ça comme une mauvaise pratique a priori, à voir
 * il vaut meiux définir des interfaces genre BackGroundWorker / UIWorker plutôt que de jouer ça à l'injection
 * cela introduit de la logique pseudo-applicative dans les modules d'injection
 */
abstract class Injector<C:Any>(modules: List<Module<in C>>) {

    private val singles = modules.flatMap { it.singles.map { it.key to it.value } }.toMap()
    private val factories = modules.flatMap { it.factories.map { it.key to it.value } }.toMap()
    private val byName = modules.flatMap { it.byName.map { it.key to it.value } }.toMap()


    private val singleInstances: MutableMap<KClass<*>, Any> = mutableMapOf()

    abstract val context: C

    fun <D : Any> get(type: KClass<D>): D {
        return when {
            singles.containsKey(type) -> {
                if (singleInstances.containsKey(type)) {
                    singleInstances[type] as D
                } else {
                    (singles[type]?.factory?.invoke(context) as D)?.apply {
                        singleInstances[type] = this
                    }
                }
            }
            factories.containsKey(type) -> {
                factories[type]?.factory?.invoke(context) as D
            }
            else -> {
                throw IllegalStateException("type def: type:${type.simpleName} non trouvée singles: ${singles.keys}  factories: ${factories.keys}")
            }
        }
    }

    inline fun
            <reified D : Any> get()
            : D {
        return get(D::class)
    }


    fun <E : Any> getByName(key:String): E{
        return (byName.get(key) as E?) ?: throw IllegalStateException("Injector, nothing injected for key \"$key\" please use injectForName")
    }


}

expect class BaseInjector : Injector<BaseInjector>

var injector: Injector<*>? = null

inline fun <reified K : Any> get(): K {
    return injector!!.get(K::class)
}

inline fun <reified K : Any> getByName(key:String): K {
    return injector!!.getByName(key)
}