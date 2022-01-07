package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.core.components.SKComponentVC
import kotlin.reflect.KClass

open class InitializationPlan(val map:Map<KClass<out SKComponentVC>, ComponentInitializer>, val screenDefault:ComponentInitializer? = null) {
    data class Import(val packageName:String, val simpleName:String)
    data class Code(val imports:List<Import> = emptyList(), val lines:List<String>) {
        fun getImportsList():List<ClassName> {
            return imports.map { ClassName(it.packageName, it.simpleName) }
        }
    }

    data class ComponentInitializer(
        val imports: List<Import> = emptyList(),
        val initBlockLines: List<String> = emptyList(),
        val onResumeLines: List<String> = emptyList(),
        val onResumeBlock: (FunSpec.Builder.()->Unit)? = null,
        val onPauseBlock: (FunSpec.Builder.()->Unit)? = null,
        val block:(TypeSpec.Builder.()->Unit)? = null
    ) {
        fun getImportsList():List<ClassName> {
            return imports.map { ClassName(it.packageName, it.simpleName) }
        }
    }
//    {
//
//        fun getImportsList():List<ClassName> {
//            return imports.map { ClassName(it.packageName, it.simpleName) }
//        }
//        fun initialize(builder:TypeSpec.Builder) {
//            builder.addInitializerBlock(
//                CodeBlock.builder().apply {
//                    codeLines.forEach {
//                        add(it)
//                    }
//                }.build()
//            )
//        }
//    }

}

