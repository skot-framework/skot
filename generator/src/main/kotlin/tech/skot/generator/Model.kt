//package tech.skot.generator
//
//import com.squareup.kotlinpoet.*
//import java.nio.file.Paths
//import kotlin.reflect.KClass
//import kotlin.reflect.KProperty
//import kotlin.reflect.full.superclasses
//
//fun generateModel(moduleName: String) {
//    val generatedDir = Paths.get("../$moduleName/generated/commonMain/kotlin")
//    val srcDir = Paths.get("../$moduleName/src/commonMain/kotlin")
//
//
//    FileSpec.builder(appPackageName, "ModelInjectorImpl")
//            .apply {
//                addType(TypeSpec.classBuilder("ModelInjectorImpl")
//                        .addSuperinterface(ClassName(appPackageName, "ModelInjector"))
//                        .addFunctions(
//                                allModelsFromApp
//                                        .map {
//                                            FunSpec.builder(it.simpleName!!.decapitalize())
//                                                    .returns(ClassName(it.packageName(),it.modelImplName()))
//                                                    .addStatement("return ${it.simpleName!!}Impl()")
//                                                    .addModifiers(KModifier.OVERRIDE)
//                                                    .build()
//                                        }
//                        ).build())
//            }.build().writeTo(generatedDir)
//    //Model skeletons
//    allModelsFromApp
//            .forEach {
//                if (!srcDir.existsClass(it.packageName(), it.modelImplName())) {
//                    FileSpec
//                            .builder(it.packageName(), it.modelImplName())
//                            .addType(
//                                    TypeSpec
//                                            .classBuilder(it.modelImplName())
//                                            .apply {
//                                                if (it.supertypes.isNotEmpty() && it.superclasses[0] != Any::class) {
//                                                    superclass(ClassName(it.superclasses[0].packageName(), it.superclasses[0].modelImplName()))
//                                                }
//                                            }
//                                            .addSuperinterface(it)
//                                            .addProperties(
//                                                    it.ownMembers().filter { it is KProperty }
//                                                            .map {
//                                                                PropertySpec
//                                                                        .builder(it.name, it.returnType.asTypeName())
//                                                                        .addModifiers(KModifier.OVERRIDE)
//                                                                        .build()
//                                                            }
//                                            )
//                                            .addFunctions(
//                                                    it.ownFuncs()
//                                                            .map {
//                                                                FunSpec
//                                                                        .builder(it.name)
//                                                                        .addModifiers(KModifier.OVERRIDE)
//                                                                        .apply {
//                                                                            if (it.isSuspend) {
//                                                                                addModifiers(KModifier.SUSPEND)
//                                                                            }
//                                                                        }
//                                                                        .returns(it.returnType.asTypeName())
//                                                                        .addParameters(
//                                                                                it.functionParameters()
//                                                                                        .map { ParameterSpec(it.name!!, it.type.asTypeName()) }
//                                                                        )
//                                                                        .addStatement(toDoToGenerate)
//                                                                        .build()
//                                                            }
//                                            )
//                                            .build()
//                            )
//                            .build()
//                            .writeTo(srcDir)
//                }
//            }
//
//}
//
//fun KClass<*>.modelImplName() = "${simpleName!!}Impl"
//
//
