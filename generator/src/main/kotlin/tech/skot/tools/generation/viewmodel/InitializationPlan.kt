package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.core.components.SKComponentVC
import kotlin.reflect.KClass

open class InitializationPlan(val map:Map<KClass<out SKComponentVC>, ComponentInitializer>) {
    data class Import(val packageName:String, val simpleName:String)

    data class ComponentInitializer(val imports:List<Import>, val codeLines:List<String>) {

        fun getImportsList():List<ClassName> {
            return imports.map { ClassName(it.packageName, it.simpleName) }
        }
        fun initialize(builder:TypeSpec.Builder) {
            builder.addInitializerBlock(
                CodeBlock.builder().apply {
                    codeLines.forEach {
                        add(it)
                    }
                }.build()
            )
        }
    }

}

