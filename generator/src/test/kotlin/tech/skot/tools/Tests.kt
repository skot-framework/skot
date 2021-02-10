package tech.skot.tools

import com.squareup.kotlinpoet.asTypeName
import tech.skot.tools.generation.viewlegacy.binding
import tech.skot.tools.generation.viewlegacy.toProxy
import java.lang.reflect.AnnotatedParameterizedType
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses
import kotlin.test.Test

abstract class EssaiAbs<B:Any>() {
    fun bidon(binding:B) {}
}

class EssaiImplViewProxy():EssaiAbs<String>() {

}

class EssaiImpl

interface Cont {
    val essai:EssaiImpl
}

class Tests {

    @Test
    fun essai() {

        val kclass = Cont::class
        val essaiType = kclass.members.find { it.name == "essai" }!!.returnType

        println((Class.forName(essaiType.asTypeName().toProxy().canonicalName)!!.kotlin.supertypes.get(0)?.arguments?.get(0)))// .find { it.simpleName == "EssaiAbs" }?.java?.annotatedSuperclass))
        println(essaiType.asTypeName().binding("coucou"))



    }
}


