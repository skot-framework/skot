package tech.skot.tools.generation

import com.squareup.kotlinpoet.TypeSpec
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewContractGenerator {


    fun copyStateInterface(state: KClass<*>) =
            state.qualifiedName?.let { name ->
                TypeSpec.interfaceBuilder(name)
                        .apply {
                            state.members.forEach {
                                when (it) {
//                                    is KProperty<*> -> {
//                                        addProperty(it.name, it.returnType)
//                                    }
                                }
                            }
                        }
                        .build()
            } ?: throw IllegalStateException("Class sans nom")

}