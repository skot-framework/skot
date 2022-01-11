package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import tech.skot.core.view.SKPermission
import tech.skot.tools.generation.*
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun Generator.generatePermissions() {
    val permissionNames = if (!permissionsInterface.existsCommonInModule(modules.viewcontract)) {
        permissionsInterface.fileInterfaceBuilder {

        }.writeTo(commonSources(modules.viewcontract))
        emptyList()
    } else {
        val skPermissionsType = typeOf<SKPermission>()
        val interfaceClass = Class.forName(permissionsInterface.canonicalName).kotlin
        interfaceClass.ownProperties().filter {
            it.returnType.isSubtypeOf(skPermissionsType)
        }
            .map { it.name }
    }

    if (!permissionsImpl.existsAndroidInModule(modules.view)) {
        permissionsImpl.fileClassBuilder(
            listOf(AndroidClassNames.manifest)
        ) {
            addSuperinterface(permissionsInterface)
            addProperties(
                permissionNames.map {
                    PropertySpec.builder(
                        name = it,
                        type = FrameworkClassNames.permissionAndroidLegacy
                    )
                        .initializer(CodeBlock.of("${FrameworkClassNames.permissionAndroidLegacy.simpleName}(name = Manifest.permission.PERMISSION_NAME)"))
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                }
            )
        } .writeTo(androidSources(feature ?: modules.view))
    }

    println("generate Permissions jvm mock .........")
    permissionsMock.fileClassBuilder() {
        addSuperinterface(permissionsInterface)
        addProperties(
            permissionNames.map {
                PropertySpec.builder(it, FrameworkClassNames.skPermissionMock, KModifier.OVERRIDE)
                    .initializer("${FrameworkClassNames.skPermissionMock.simpleName}(\"$it\")")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))

}