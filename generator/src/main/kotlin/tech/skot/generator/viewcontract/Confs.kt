package tech.skot.generator.viewcontract

import com.squareup.kotlinpoet.*
import tech.skot.contract.viewcontract.ComponentView
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.superclasses

fun KClass<out ComponentView>.buildConf(): FileSpec {

    val confClassName = "${simpleName}Conf"
    val properties =
            ownMembers()
                    .filter { it is KProperty }
                    .filter {
                        !it.returnType.isComponentView() && !it.returnType.isCollectionOfComponentView()
                    }
    val confClass =
            TypeSpec.classBuilder(confClassName)
                    .primaryConstructor(
                            FunSpec.constructorBuilder()
                                    .addParameters(
                                            properties.map {
                                                ParameterSpec(it.name, it.returnType.asTypeName())
                                            }
                                    ).build()
                    )
                    .addProperties(
                            properties
                                    .map {
                                        PropertySpec.builder(it.name, it.returnType.asTypeName())
                                                .initializer(it.name)
                                                .build()
                                    }
                    )



    return FileSpec.builder(packageName(), confClassName)
            .apply {
                addType(confClass.build())
            }.build()
}


fun KClass<*>.packageName() = this.java.`package`.name
fun KType.isAny() = this.classifier == Any::class
fun KClass<*>.ownMembers(): List<KCallable<*>> {
    val superMembersNames = superclasses[0].members.map { it.name }
    return members.filter { !superMembersNames.contains(it.name) }
}

val componentViewType = ComponentView::class.createType()
val collectionType = Collection::class.createType(arguments = listOf(KTypeProjection(KVariance.OUT, componentViewType)))
fun KType.isComponentView() = isSubtypeOf(componentViewType)
fun KType.isCollectionOfComponentView() = isSubtypeOf(collectionType)