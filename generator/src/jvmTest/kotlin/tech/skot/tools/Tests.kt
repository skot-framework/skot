package tech.skot.tools

import com.squareup.kotlinpoet.asTypeName
import tech.skot.tools.generation.*
import tech.skot.tools.generation.viewlegacy.binding
import tech.skot.tools.generation.viewlegacy.toProxy
import java.lang.reflect.AnnotatedParameterizedType
import java.nio.file.Paths
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

//        val kclass = Cont::class
//        val essaiType = kclass.members.find { it.name == "essai" }!!.returnType
//
//        println((Class.forName(essaiType.asTypeName().toProxy().canonicalName)!!.kotlin.supertypes.get(0)?.arguments?.get(0)))// .find { it.simpleName == "EssaiAbs" }?.java?.annotatedSuperclass))
//        println(essaiType.asTypeName().binding("coucou"))

    }


    @Test
    fun extractStringPatterns() {

        val chaineTest = "chaîne test %d   et %s   %ser %i"

        val regex = Regex("(%[a-zA-Z])")
        val res = regex.findAll(chaineTest).map {
            it.groupValues[1]
        }.joinToString(" ")

        assertEquals(
            "%d %s %s %i",
            res
        )
    }

    interface TestPrim {
        val prim1:Char
        val prim2:Byte
        val prim3:Short
        val prim4:Int
        val prim5:Float
        val prim6:Double
    }



    @Test
    fun testPrims(){

        TestPrim::class.ownProperties().forEach {
            println(it.returnType.primitiveDefaultInit())
        }


    }
}


