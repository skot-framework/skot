package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.superclasses

class ParamInfos(
    val name: String,
    val typeName: TypeName,
    val modifiers: List<KModifier> = emptyList(),
    val isVal: Boolean = true,
    val mutable: Boolean = false,
    val isPrivate: Boolean = false,
    val default:String? = null
)

fun TypeSpec.Builder.addPrimaryConstructorWithParams(vals: List<ParamInfos>): TypeSpec.Builder {
    primaryConstructor(
        FunSpec.constructorBuilder()
            .apply {
                vals
                    .forEach {
                        addParameter(
                            ParameterSpec.builder(it.name, it.typeName, it.modifiers)
                                .apply {
                                    if (it.default != null) {
                                        defaultValue(it.default)
                                    }
                                }
                                .build()
                        )

                    }
            }
            .build()
    )
    addProperties(
        vals.filter { it.isVal }.map {
            PropertySpec.builder(it.name, it.typeName)
                .mutable(it.mutable)
                .addModifiers(it.modifiers)
                .initializer(it.name)
                .apply {
                    if (it.isPrivate) {
                        addModifiers(KModifier.PRIVATE)
                    }
                }
                .build()
        }
    )
    return this
}


fun TypeName.nullable() = this.copy(true)

fun TypeName.simpleName(): String? = when {
    (this is ClassName) -> simpleName
    (this is ParameterizedTypeName) -> this.rawType.simpleName
    else -> null
}

fun FileSpec.Builder.addImportClassName(className: ClassName) =
    addImport(className.packageName, className.simpleName)

fun FileSpec.Builder.addImportTypeName(typeName: TypeName) =
    when(typeName) {
        is ParameterizedTypeName -> addImportClassName(typeName.rawType)
        else -> addImportClassName(typeName as ClassName)
    }

fun String.packageToPathFragment() = replace('.', '/')

fun String.fullNameAsClassName() =
    ClassName(substring(0, lastIndexOf('.')), substring(lastIndexOf('.') + 1))

fun ClassName.fileClassBuilder(
    imports: List<ClassName> = emptyList(),
    block: TypeSpec.Builder.() -> Unit
) = FileSpec.builder(packageName, simpleName)
    .addType(
        TypeSpec.classBuilder(simpleName)
            .apply(block)
            .build()
    )
    .apply {
        imports.forEach {
            addImportClassName(it)
        }
    }
    .build()


fun ClassName.fileInterfaceBuilder(
    imports: List<ClassName> = emptyList(),
    block: TypeSpec.Builder.() -> Unit
) = FileSpec.builder(packageName, simpleName)
    .addType(
        TypeSpec.interfaceBuilder(simpleName)
            .apply(block)
            .build()
    )
    .apply {
        imports.forEach {
            addImportClassName(it)
        }
    }
    .build()

fun ClassName.fileObjectBuilder(
    imports: List<ClassName> = emptyList(),
    block: TypeSpec.Builder.() -> Unit
) = FileSpec.builder(packageName, simpleName)
    .addType(
        TypeSpec.objectBuilder(simpleName)
            .apply(block)
            .build()
    )
    .apply {
        imports.forEach {
            addImportClassName(it)
        }
    }
    .build()

fun Path.replaceSegment(segment: String, replacement: String): Path =
    map {
        if (it.toString() == segment) {
            replacement
        } else {
            it.toString()
        }
    }.let {
        Paths.get(it.joinToString("/", prefix = if (this.startsWith("/")) "/" else ""))
    }


fun KClass<*>.ownMembers(): List<KCallable<*>> {
    val superMembersNames = superclasses.flatMap { it.members.map { it.name } }
    return members.filter { !superMembersNames.contains(it.name) }
}

fun KClass<*>.ownProperties(): List<KCallable<*>> {
    val superTypePropertiesNames =
        superclasses[0].members.filter { it is KProperty }.map { it.name }
    return members.filter { it is KProperty }.filter { !superTypePropertiesNames.contains(it.name) }
}

fun KClass<*>.ownFuncs() = ownMembers().filterIsInstance(KFunction::class.java)