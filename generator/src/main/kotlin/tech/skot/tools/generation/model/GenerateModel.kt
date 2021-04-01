package tech.skot.tools.generation.model

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.Modules
import tech.skot.tools.generation.fileClassBuilder
import tech.skot.tools.generation.fullNameAsClassName

fun Generator.generateModel() {
    println("-----generateModel")
    deleteModuleGenerated(Modules.model)
    components.forEach {
        if (it.hasModel()) {
            //un model a été défini (par convention de nommage)
            //on va générer l'implémenation si elle n'existe pas encore et l'intégrer au modelInjector

            println("Un Model contract trouvé pour ${it.name}")

            if (!it.model().existsCommonInModule(Modules.model)) {
                println("pas d'implémentation trouvée on génère un squelette")

                it.modelContract().canonicalName.fullNameAsClassName()

                it.model().fileClassBuilder {
                    addSuperinterface(it.modelContract())
                }
                        .writeTo(commonSources(Modules.model))
            }
        }
    }

    modelInjectorImpl.fileClassBuilder(
            componentsWithModel.map { it.model() })
    {
        addSuperinterface(modelInjectorInterface)
        addFunctions(
                componentsWithModel.map {
                    FunSpec.builder(it.name.decapitalize())
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(it.modelContract())
                            .addCode("return ${it.model().simpleName}()")
                            .build()
                }
        )
    }.writeTo(generatedCommonSources(Modules.model))
}