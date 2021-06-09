package tech.skot.tools

import com.squareup.kotlinpoet.asTypeName
import tech.skot.tools.generation.combinaisons
import tech.skot.tools.generation.getDocument
import tech.skot.tools.generation.getElementsWithTagName
import tech.skot.tools.generation.replaceSegment
import tech.skot.tools.generation.viewlegacy.binding
import tech.skot.tools.generation.viewlegacy.toProxy
import java.lang.reflect.AnnotatedParameterizedType
import java.nio.file.Paths
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

//        val kclass = Cont::class
//        val essaiType = kclass.members.find { it.name == "essai" }!!.returnType
//
//        println((Class.forName(essaiType.asTypeName().toProxy().canonicalName)!!.kotlin.supertypes.get(0)?.arguments?.get(0)))// .find { it.simpleName == "EssaiAbs" }?.java?.annotatedSuperclass))
//        println(essaiType.asTypeName().binding("coucou"))

    }

    @Test
    fun testParse() {
        val path = java.nio.file.Paths.get("/Users/mscotet/UA/sezane/octobre/app-android/view/src/androidMain/res/layout/personnal_infos.xml")
        val doc = path.getDocument()
        println(doc.getElementsWithTagName("include").map {
            it.getAttribute("android:id")
        })
    }
}


