package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.jvm.jvmErasure

class ParamInfos(val name:String, val typeName: TypeName, val modifiers:List<KModifier> = emptyList(), val isVal:Boolean = true)

fun TypeSpec.Builder.addPrimaryConstructorWithParams(vals: List<ParamInfos>): TypeSpec.Builder {
    primaryConstructor(
            FunSpec.constructorBuilder()
                    .apply {
                        vals
                                .forEach {
                                    addParameter(it.name, it.typeName, it.modifiers)
                                }
                    }
                    .build()
    )
    addProperties(
            vals.filter { it.isVal }.map {
                PropertySpec.builder(it.name, it.typeName)
                        .addModifiers(it.modifiers)
                        .initializer(it.name)
                        .build()
            }
    )
    return this
}


fun TypeName.nullable() = this.copy(true)

fun FileSpec.Builder.addImportClassName(className: ClassName) =
    addImport(className.packageName, className.simpleName)


fun String.packageToPathFragment() = replace('.','/')

fun String.fullNameAsClassName() = ClassName(substring(0,lastIndexOf('.')),substring(lastIndexOf('.')))

fun ClassName.fileClassBuilder(imports:List<ClassName> = emptyList(),block:TypeSpec.Builder.()->Unit) = FileSpec.builder(packageName, simpleName)
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


fun ClassName.fileInterfaceBuilder(imports:List<ClassName> = emptyList(),block:TypeSpec.Builder.()->Unit) = FileSpec.builder(packageName, simpleName)
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

