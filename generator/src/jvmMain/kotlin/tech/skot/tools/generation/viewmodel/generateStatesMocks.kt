package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*

@ExperimentalStdlibApi
fun Generator.generateStatesMocks(rootState: StateDef) {

    fun StateDef.generateMock() {

        mockClassName.fileClassBuilder {
            addSuperinterface(defClassName)
            addSuperinterface(contractClassName)
            addPrimaryConstructorWithParams(
                parentsList.map {
                    ParamInfos(it.name.decapitalizeAsciiOnly(), it.mockClassName)
                } + compositeParts.map {
                    ParamInfos(it.name.decapitalizeAsciiOnly(), it.mock, modifiers = listOf(KModifier.OVERRIDE))
                } +
                properties.map {
                    ParamInfos(
                        it.name,
                        it.typeName,
                        modifiers = listOf(KModifier.OVERRIDE),
                        isVal = true,
                        mutable = true,
                    )
                }
            )
//            (subStates + compositeSubStates).forEach {
            (subStates + compositeSubStates).forEach {
                    addProperty(
                        PropertySpec.builder(
                            it.nameAsProperty.suffix("SKData"),
                            FrameworkClassNames.skManualData.parameterizedBy(it.mockClassName.nullable())
                        )
                            .addModifiers(KModifier.OVERRIDE)
                            .initializer("SKManualData(null)")
                            .build()
                    )

                    addProperty(
                        PropertySpec.builder(
                            it.nameAsProperty,
                            it.mockClassName.nullable()
                        )
                            .mutable(true)
                            .addModifiers(KModifier.OVERRIDE)
                            .delegate(it.nameAsProperty.suffix("SKData"))
                            .build()

                    )
                }


        }.writeTo(generatedJvmTestSources(modules.viewmodel))


        subStates.forEach {
            it.generateMock()
        }

        compositeSubStates.forEach {
            it.generateMock()
        }
    }

    rootState.generateMock()


}