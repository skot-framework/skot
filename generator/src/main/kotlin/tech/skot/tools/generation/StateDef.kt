package tech.skot.tools.generation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import tech.skot.model.SKBms
import tech.skot.model.SKBySKData
import tech.skot.model.SKWithDefault
import tech.skot.model.SKCompositeStateDef
import tech.skot.model.SKCompositeStateParts
import tech.skot.model.SKStateDef
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf


@ExperimentalStdlibApi
class StateDef(
    val nameAsProperty: String,
    appPackage: String,
    val kclass: KClass<*>,
    val parentsList: List<StateDef> = emptyList(),
    val propertiesComposingComposite: List<StateDef>? = null
) {

    class Property(val name: String, val typeName: TypeName, val mutable: Boolean, val bySkData:Boolean, val default:String?)
    class CompositePartDef(
        val name: String,
        val contract: ClassName,
        val infos: ClassName,
        val model: ClassName,
        val kClass: KClass<SKStateDef>
    )

    init {
        if (!kclass.simpleName!!.endsWith("Def")) {
            throw IllegalStateException("State definition's class ${kclass.simpleName} must ends with \"Def\"")
        }
    }

    val isCompositeState: Boolean = kclass.isSubclassOf(SKCompositeStateDef::class)

    val compositeParts: List<CompositePartDef> = if (isCompositeState) {
        kclass.superclasses.filter { it != SKStateDef::class && it.isSubclassOf(SKStateDef::class) }
            .map {
                val compName = it.simpleName!!.withOut("Def")
                CompositePartDef(
                    compName,
                    contract = ClassName(it.packageName(), compName.suffix("Contract")),
                    infos = ClassName(it.packageName(), compName.suffix("Infos")),
                    model = ClassName(it.packageName(), compName),
                    kClass = it as KClass<SKStateDef>
                )
            }
    } else {
        emptyList()
    }


    val name: String = kclass.simpleName!!.withOut("Def")

    init {
        println("------ state $name isComposite ? $isCompositeState")
    }


    val contractClassName = ClassName(kclass.packageName(), name.suffix("Contract"))
    val modelClassName = ClassName(kclass.packageName(), name)
    val infosClassName = ClassName(kclass.packageName(), name.suffix("Infos"))

    val properties: List<Property> =
        kclass.ownProperties()
            .filter { !it.returnType.isSKStateC() }
            .map {
                Property(it.name, it.returnType.asTypeName(), it is KMutableProperty, it.hasAnnotation<SKBySKData>(), it.findAnnotation<SKWithDefault>()?.initilization)
            }

    init {
        if (parentsList.isNotEmpty() && properties.none { !it.mutable }) {
            throw IllegalStateException("A state need at least one immutable property, if not you don't really need it")
        }
    }

    val subStates: List<StateDef> =
        kclass.ownProperties()
            .filter {
                it.returnType.isSKStateC() && !it.returnType.isSKStateComposite()
            }
            .map {
                if (!it.returnType.isMarkedNullable || (it is KMutableProperty)) {
                    throw IllegalStateException("Sub-states must be declared as immutable properties (\"val\") and nullable")
                }
                StateDef(
                    it.name,
                    appPackage,
                    it.returnType.jvmErasure as KClass<SKStateDef>,
                    parentsList + this
                )
            }

    val compositeSubStates: List<StateDef> =
        kclass.ownProperties()
            .filter {
                it.returnType.isSKStateComposite()
            }
            .map {
                if (!it.returnType.isMarkedNullable || (it is KMutableProperty)) {
                    throw IllegalStateException("Composite Sub-states must be declared as immutable properties (\"val\") and nullable")
                }
                val propertiesComposing =
                    (it.findAnnotation<SKCompositeStateParts>()
                        ?: throw IllegalStateException("You have to define, using @SKCompositeStateParts which substates are composing ${it.name}"))
                        .composingStatesNames?.map { compositePartPropertyName ->
                            subStates.find { it.nameAsProperty == compositePartPropertyName }
                                ?: throw IllegalStateException("$compositePartPropertyName is not a substate")
                        }
                StateDef(
                    it.name,
                    appPackage,
                    it.returnType.jvmErasure as KClass<SKStateDef>,
                    parentsList + this,
                    propertiesComposing
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

    init {
        println("State :$name  Bms : ${bmS.joinToString { it.simpleName }}")
    }

}

@ExperimentalStdlibApi
fun KType.isSKStateC(): Boolean =
    isMarkedNullable && isSubtypeOf(typeOf<SKStateDef?>()) || !isMarkedNullable && isSubtypeOf(
        typeOf<SKStateDef>()
    )

@ExperimentalStdlibApi
fun KType.isSKStateComposite(): Boolean =
    isMarkedNullable && isSubtypeOf(typeOf<SKCompositeStateDef?>()) || !isMarkedNullable && isSubtypeOf(
        typeOf<SKCompositeStateDef>()
    )