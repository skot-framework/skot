package tech.skot.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.components.ComponentView
import tech.skot.components.ScreenView
import java.nio.file.Paths
import kotlin.reflect.*
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure


class ViewGenerator(
        viewNode: ViewNode,
        baseStrActivity: String,
        baseStrFragment: String,
        mapActionsStrActivity: Map<KClass<*>, String>,
        mapActionsStrFragment: Map<KClass<*>, String>,
        mapComponentStrActivity: Map<KClass<out ComponentView>, String>,
        mapComponentStrFragment: Map<KClass<out ComponentView>, String>) {

    val baseActivity = baseStrActivity.toClassName()
    val baseFragment = baseStrFragment.toClassName()
    val mapActionsActivity = mapActionsStrActivity.mapValues { it.value.toClassName() }
    val mapActionsFragment = mapActionsStrFragment.mapValues { it.value.toClassName() }
    val mapComponentActivity = mapComponentStrActivity.mapValues { it.value.toClassName() }
    val mapComponentFragment = mapComponentStrFragment.mapValues { it.value.toClassName() }

    init {
        initGenerator(viewNode)
    }

    fun generateView(moduleName: String) {
        val generatedDir = Paths.get("../$moduleName/generated/main/kotlin")
        val srcDir = Paths.get("../$moduleName/srcGenerated/main/kotlin")

        actions
                .forEach {

                    it.buildActionsActions().writeTo(generatedDir)


                    val actionImpl = it.actionsImplName()
                    val packageName = it.packageName()

                    if (!srcDir.existsClass(packageName, actionImpl)) {
                        it.buildActionsImplSkeletonFile().writeTo(srcDir)
                    }
                }

        componentsFromApp
                .forEach { it.buildViewImplGen().writeTo(generatedDir) }

        generateViewsInjector().writeTo(generatedDir)
    }

    fun KClass<*>.buildActionsActions(): FileSpec {
        return FileSpec
                .builder(packageName(), actionsActionName())
                .addType(
                        TypeSpec
                                .classBuilder(actionsActionName())
                                .addModifiers(KModifier.SEALED)
                                .superclass(ClassName("tech.skot.view", "Action"))
                                .addTypes(
                                        ownMembers()
                                                .filter { it is KFunction }
                                                .map {
                                                    val funcParamaters = it.functionParameters()
                                                    if (funcParamaters.isNotEmpty()) {
                                                        TypeSpec.classBuilder(it.name.capitalize())
                                                                .superclass(ClassName(packageName(), actionsActionName()))
                                                                .addPrimaryConstructorWithParams(
                                                                        funcParamaters
                                                                                .map { ParamInfos(it.name!!, it.type.asTypeName()) }
                                                                )
                                                                .build()
                                                    }
                                                    else {
                                                        TypeSpec.objectBuilder(it.name.capitalize())
                                                                .superclass(ClassName(packageName(), actionsActionName()))
                                                                .build()
                                                    }

                                                }
                                )
                                .build()
                )
                .addFunction(
                        FunSpec.builder("treatAction")
                                .addParameter("action", ClassName(packageName(), actionsActionName()))
                                .receiver(this)
                                .beginControlFlow("when (action)")
                                .apply {
                                    ownMembers()
                                            .filter { it is KFunction }
                                            .forEach {
                                                val funcParameters = it.functionParameters()
                                                addStatement("${if (funcParameters.isNotEmpty()) "is " else ""}${actionsActionName()}.${it.name.capitalize()} -> ${it.name}(${funcParameters.map {
                                                    "action.${it.name}"
                                                }.joinToString(", ")})")
                                            }
                                }
                                .endControlFlow()
                                .build()
                )
                .build()
    }

    fun KClass<*>.buildActionsImplSkeletonFile(): FileSpec {

//        val containerName = "container"
//        val containerClassName = ClassName("tech.skot.view", "Container")

        return FileSpec
                .builder(packageName(), actionsImplName())
                .addType(
                        TypeSpec.classBuilder(actionsImplName())
                                .addSuperinterface(this)
                                .addPrimaryConstructorWithParams(
                                        listOf(
                                                ParamInfos("activity", actionActivityClass(), listOf(KModifier.PRIVATE)),
                                                ParamInfos("fragment", actionFragmentClass(), listOf(KModifier.PRIVATE))
                                        )
                                )
                                .addFunctions(
                                        ownMembers()
                                                .filter {
                                                    it is KFunction
                                                }
                                                .map {
                                                    FunSpec.builder(it.name)
                                                            .addModifiers(KModifier.OVERRIDE)
                                                            .addParameters(
                                                                    it.parameters.filter { it.kind != KParameter.Kind.INSTANCE }.map {
                                                                        ParameterSpec.builder(it.name!!, it.type.asTypeName())
                                                                                .build()
                                                                    })
                                                            .addCode(toDoToGenerate)
                                                            .build()
                                                }
                                )
                                .build()
                )
                .build()


    }

    fun KClass<out ComponentView>.buildViewImplGen(): FileSpec {
        val thisActions = actions().map { it.jvmErasure }
        val subComponentsMembers = subComponentMembers()
        return FileSpec
                .builder(packageName(), viewImplGenName())
                .apply {
                    thisActions.forEach {
                        addImport(it.packageName(), it.actionsActionName(), "treatAction")
                    }
                    if (subComponentsMembers.isNotEmpty()) {
                        addImport("tech.skot.components", "ComponentViewImpl")
                        subComponentsMembers.forEach {
                            addImportClassName(it.returnType.componentView()!!.activityClassBound())
                            addImportClassName(it.returnType.componentView()!!.fragmentClassBound())
                        }
                    }

                }
                .addType(
                        TypeSpec
                                .classBuilder(viewImplGenName())
                                .addModifiers(KModifier.ABSTRACT)
                                .apply {
                                    if (!isActualComponent()) {
                                        addTypeVariables(listOf(
                                                TypeVariableName("A", activityClassBound()),
                                                TypeVariableName("F", fragmentClassBound())
                                        ))
                                    }
                                }
                                .superclass(
                                        superView()
                                                .viewImplClassName()
                                                .parameterizedBy(activityClass(), fragmentClass())
                                )
                                .addPrimaryConstructorWithParams(
                                        subComponentsMembers.map {
                                            ParamInfos(it.name, it.returnType.viewImplClassName()!!, isVal = true, modifiers = listOf(KModifier.OVERRIDE))
                                        }
                                                +
                                        propertyMember().map {
                                            if (it is KMutableProperty) {
                                                ParamInfos(it.name, it.returnType.asTypeName(), isVal = false)
                                            } else {
                                                ParamInfos(it.name, it.returnType.asTypeName(), modifiers = listOf(KModifier.OVERRIDE), isVal = true)
                                            }

                                        }
                                )
                                .addSuperinterface(this)
                                .addProperties(
                                        propertyMember()
                                                .filterIsInstance(KMutableProperty::class.java)
                                                .flatMap {
                                                    listOf(
                                                            PropertySpec.builder(it.ldName(),
                                                                    ClassName("tech.skot.view.live", "MutableSKLiveData")
                                                                            .parameterizedBy(it.returnType.asTypeName()))
                                                                    .addModifiers(KModifier.PRIVATE)
                                                                    .initializer("MutableSKLiveData(${it.name})")
                                                                    .build(),
                                                            PropertySpec.builder(it.name, it.returnType.asTypeName())
                                                                    .mutable(true)
                                                                    .addModifiers(KModifier.OVERRIDE)
                                                                    .getter(
                                                                            FunSpec.getterBuilder()
                                                                                    .addCode("return ${it.ldName()}.value\n")
                                                                                    .build()
                                                                    )
                                                                    .setter(FunSpec.setterBuilder()
                                                                            .addParameter("newVal", it.returnType.asTypeName())
                                                                            .addCode("${it.ldName()}.postValue(newVal)")
                                                                            .build())
                                                                    .build()

                                                    )
                                                }
                                )
                                .apply {
                                    if (isActualComponent() && isInstance(ScreenView::class)) {
                                        addFunction(
                                                FunSpec.builder("getActivityClass")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addStatement("return ${activityClassBound().simpleName}::class")
                                                        .build()
                                        )
                                        addFunction(
                                                FunSpec.builder("createFragment")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addStatement("return ${fragmentClassBound().simpleName}()")
                                                        .build()
                                        )


                                    }
                                }
                                .addFunctions(
                                        propertyMember()
                                                .map {
                                                    FunSpec.builder(it.onMethodName())
                                                            .addModifiers(KModifier.ABSTRACT)
                                                            .addParameter(it.name, it.returnType.asTypeName())
                                                            .build()
                                                }
                                )
                                .apply {

                                    val members = propertyMember()
                                    if (members.isNotEmpty() || subComponentsMembers.isNotEmpty()) {
                                        addFunction(
                                                FunSpec.builder("linkTo")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("lifecycleOwner", lifecycleOwnerClassName)
                                                        .addCode("super.linkTo(lifecycleOwner)\n")
                                                        //appel des link des sous-composants
                                                        .apply {


                                                            subComponentsMembers
                                                                    .forEach {
                                                                        if (it.returnType.isCollectionOfComponentView()) {
                                                                            addStatement("${it.name}.forEach { (it as ComponentViewImpl<*,*>).linkTo(lifecycleOwner)}\n")
                                                                        } else {
                                                                            addStatement("(${it.name} as ComponentViewImpl<*,*>).linkTo(lifecycleOwner)\n")
                                                                        }

                                                                    }

                                                            members
                                                                    .forEach {
                                                                        if (it is KMutableProperty) {
                                                                            beginControlFlow("${it.ldName()}.observe(lifecycleOwner)")
                                                                            addStatement("${it.onMethodName()}(it)")
                                                                            endControlFlow()
                                                                        } else {
                                                                            addStatement("${it.onMethodName()}(${it.name})")
                                                                        }
                                                                    }
                                                        }
                                                        .build()
                                        )
                                    }
                                }

                                .apply {

                                    if (thisActions.isNotEmpty()) {

                                        addProperties(
                                                thisActions
                                                        .map {
                                                            PropertySpec.builder(it.actionsImplName().decapitalize(), it.actionsImpClasslName())
                                                                    .addModifiers(KModifier.LATEINIT)
                                                                    .mutable(true)
                                                                    .build()
                                                        }
                                        )

                                        addFunctions(
                                                thisActions
                                                        .flatMap {actionsClass ->

                                                            actionsClass.ownMembers()
                                                                    .filter { it is KFunction }
                                                                    .map {
                                                                        val funcParameters = it.functionParameters()
                                                                        FunSpec.builder(it.name)
                                                                                .addParameters(
                                                                                        it.functionParameters().map {
                                                                                            ParameterSpec.builder(it.name!!, it.type.asTypeName())
                                                                                                    .build()
                                                                                        }
                                                                                )
                                                                                .addModifiers(KModifier.OVERRIDE)
                                                                                .addStatement("messages.post(${actionsClass.actionsActionName()}.${it.name.capitalize()}${
                                                                                if (funcParameters.isNotEmpty()) {
                                                                                    funcParameters.map { it.name }.joinToString(", ", "(", ")")
                                                                                }
                                                                                else ""})")
                                                                                .build()
                                                                    }
                                                        }
                                        )

                                        addFunction(
                                                FunSpec.builder("initWith")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("activity", activityClass())
                                                        .addParameter("fragment", fragmentClass().nullable())
                                                        .addStatement("super.initWith(activity, fragment)")
                                                        .apply {
                                                            subComponentsMembers
                                                                    .forEach {
                                                                        val subCompType = "ComponentViewImpl<${it.returnType.componentView()!!.activityClassBound().simpleName},${it.returnType.componentView()!!.fragmentClassBound().simpleName}>"
                                                                        if (it.returnType.isCollectionOfComponentView()) {
                                                                            addStatement("${it.name}.forEach { (it as $subCompType).initWith(activity, fragment)}")
                                                                        } else {
                                                                            addStatement("(${it.name} as $subCompType).initWith(activity, fragment)")
                                                                        }

                                                                    }
                                                            thisActions.forEach {
                                                                addStatement("${it.actionsImplName().decapitalize()} = ${it.actionsImplName()}(activity, fragment)")
                                                            }
                                                        }
                                                        .build()
                                        )

                                        addFunction(
                                                FunSpec.builder("treatAction")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("action", actionClassName)
                                                        .beginControlFlow("when (action)")
                                                        .apply {
                                                            thisActions.forEach {
                                                                addStatement("is ${it.actionsActionName()} -> ${it.actionsImplName().decapitalize()}.treatAction(action)")
                                                            }
                                                        }
                                                        .addStatement("else -> super.treatAction(action)")
                                                        .endControlFlow()
                                                        .build()
                                        )


                                    }
                                }

                                .build()
                )
                .build()
    }

    val viewInjectorImplName = "ViewInjectorImpl"

    fun generateViewsInjector() =
            FileSpec.builder(appPackageName, viewInjectorImplName)
                    .addType(
                            TypeSpec.classBuilder(viewInjectorImplName)
                                    .addSuperinterface(ClassName(appPackageName, "ViewInjector"))
                                    .addFunctions(
                                            componentsFromApp
                                                    .filter { it.isActualComponent() }
                                                    .map {
                                                        FunSpec.builder(it.compName().decapitalize())
                                                                .addModifiers(KModifier.OVERRIDE)
                                                                .addParameters(
                                                                        it.ownMembers()
                                                                                .filterIsInstance(KProperty::class.java)
                                                                                .map {
                                                                                    ParameterSpec(it.name, it.returnType.asTypeName())
                                                                                }

                                                                )
                                                                .addStatement("return ${it.viewImplName()}(${
                                                                    it.ownMembers()
                                                                            .filterIsInstance(KProperty::class.java)
                                                                            .map { it.name }
                                                                            .joinToString(", ")
                                                                })")
                                                                .build()
                                                    }
                                    )
                                    .build()
                    )
                    .build()


    fun KProperty<*>.onMethodName() = "on${name.capitalize()}"
    fun KMutableProperty<*>.ldName() = "${name}LD"
    fun KClass<*>.actionActivityClass() = mapActionsActivity.getOrDefault(this, baseActivity)
    fun KClass<*>.actionFragmentClass() = mapActionsFragment.getOrDefault(this, baseFragment)

    //TODO on peut éviter beaucoup de paramétrage en prenant les paramètres de la super classe et des interfaces si != base
    //(sans dépendance à view pour autant
    fun KClass<out ComponentView>.activityClassBound() = mapComponentActivity.getOrDefault(this, baseActivity)

    fun KClass<out ComponentView>.fragmentClassBound() = mapComponentFragment.getOrDefault(this, baseFragment)

    fun KClass<out ComponentView>.activityClass() = if (isActualComponent()) activityClassBound() else TypeVariableName("A")

    fun KClass<out ComponentView>.fragmentClass() = if (isActualComponent()) fragmentClassBound() else TypeVariableName("F")


}

fun TypeName.nullable() = this.copy(true)


fun KClass<*>.actionsActionName() = "${simpleName!!}Action"
fun KClass<*>.actionsActionClassName() = ClassName(packageName(), actionsActionName())


fun KCallable<*>.functionParameters() = parameters.filter { it.kind == KParameter.Kind.VALUE }


fun KClass<*>.actionsImplName() = "${simpleName!!}Impl"
fun KClass<*>.actionsImpClasslName() = ClassName(packageName(), actionsImplName())


fun KClass<out ComponentView>.viewImplName() = "${simpleName!!}Impl"

fun KClass<out ComponentView>.viewImplGenName() = "${viewImplName()}Gen"

fun KClass<out ComponentView>.viewImplClassName() = ClassName(packageName(), viewImplName())



