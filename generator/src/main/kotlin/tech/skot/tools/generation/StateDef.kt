package tech.skot.tools.generation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import tech.skot.model.SKBms
import tech.skot.model.SKStateDef
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class StateDef(
    val nameAsProperty: String,
    appPackage: String,
    val kclass: KClass<*>,
    val parentsList: List<StateDef> = emptyList()
) {

    class Property(val name: String, val typeName: TypeName, val mutable: Boolean)

    val name: String = kclass.simpleName!!.withOut("Def")

    val contractClassName = ClassName(kclass.packageName(), name.suffix("Contract"))
    val modelClassName = ClassName(kclass.packageName(), name)
    val infosClassName = ClassName(kclass.packageName(), name.suffix("Infos"))

    val properties: List<Property> =
        kclass.ownProperties()
            .filter { !it.returnType.isSKStateC() }
            .map {
                Property(it.name, it.returnType.asTypeName(), it is KMutableProperty)
            }


    val subStates: List<StateDef> =
        kclass.ownProperties()
            .filter {
                it.returnType.isSKStateC()
            }
            .map {
                StateDef(
                    it.name,
                    appPackage,
                    it.returnType.jvmErasure as KClass<SKStateDef>,
                    parentsList + this
                )
            }

    val bmS: List<ClassName> =
        kclass.findAnnotation<SKBms>()?.bMs?.map { bm ->
            if (bm.contains(".")) {
                bm.fullNameAsClassName()
            } else {
                ClassName("$appPackage.model.business", bm)
            }

        } ?: emptyList()


}

@ExperimentalStdlibApi
fun KType.isSKStateC(): Boolean =
    isMarkedNullable && isSubtypeOf(typeOf<SKStateDef?>()) || !isMarkedNullable && isSubtypeOf(
        typeOf<SKStateDef>()
    )
