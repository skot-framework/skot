package tech.skot.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.components.ComponentView
import tech.skot.components.ScreenView
import tech.skot.components.WebView
import tech.skot.contract.LinkManually
import tech.skot.contract.Private
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.*
import kotlin.reflect.jvm.jvmErasure

val mapCoreCompViewAndroidView = mapOf(
        WebView::class to "android.webkit.WebView"
)

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
        val srcDir = Paths.get("../$moduleName/src/main/kotlin")

        actions
                .forEach {
                    it.buildActionsActions().writeTo(generatedDir)

                    if (!srcDir.existsClass(it.packageName(), it.actionsImplName())) {
                        it.buildActionsImplSkeletonFile().writeTo(srcDir)
                    }
                }

        componentsFromApp
                .forEach {
                    it.buildViewImplGen().writeTo(generatedDir)

                    if (it.ownFuncs().isNotEmpty()) {
                        it.buildCompActions().writeTo(generatedDir)
                    }

                    val sketletonFileSpec = it.buildViewImplSkeleton()
                    val pathOfSkeleton = srcDir.pathOfClass(it.packageName(), it.viewImplName())

                    if (!Files.exists(pathOfSkeleton)) {
                        sketletonFileSpec.writeTo(srcDir)
                    } else {
                        val skeleton = StringBuilder()
                        sketletonFileSpec.writeTo(skeleton)
                        val textSkeleton = skeleton.toString()
                        val newConstructorsRange = textSkeleton.constructorsRange(it.viewImplName())

                        val newConstructor = textSkeleton.subSequence(newConstructorsRange)

                        val viewImplFile = pathOfSkeleton.toFile()
                        val currentImpl = viewImplFile.readText()
                        val oldConstructorsRange = currentImpl.constructorsRange(it.viewImplName())

                        if (newConstructor != currentImpl.subSequence(oldConstructorsRange)) {
                            viewImplFile.writeText(currentImpl.replaceRange(oldConstructorsRange, newConstructor))
                        }

                    }


                }

        generateViewsInjector().writeTo(generatedDir)
    }

    //Attention si des lambdas dans les paramètres
    //à améliorer
    private fun String.constructorsRange(className: String): IntRange {
        val start = indexOf("class $className")
        val nextOpenBody = indexOf("{", start) - 2
        val end = if (nextOpenBody == -3) (this.length - 1) else nextOpenBody
        return IntRange(start, end)
    }

    fun KClass<out ComponentView>.buildCompActions(): FileSpec {
        return FileSpec
                .builder(packageName(), compActionName())
                .addType(
                        buildActionType(compActionName())
                )
                .build()
    }

    fun KClass<*>.buildActionType(name: String) = TypeSpec
            .classBuilder(name)
            .addModifiers(KModifier.SEALED)
            .superclass(ClassName("tech.skot.view", "Action"))
            .addTypes(
                    ownFuncs()
                            .map {
                                val funcParamaters = it.functionParameters()
                                if (funcParamaters.isNotEmpty()) {
                                    TypeSpec.classBuilder(it.name.capitalize())
                                            .superclass(ClassName(packageName(), name))
                                            .addPrimaryConstructorWithParams(
                                                    funcParamaters
                                                            .map { ParamInfos(it.name!!, it.type.asTypeName()) }
                                            )
                                            .build()
                                } else {
                                    TypeSpec.objectBuilder(it.name.capitalize())
                                            .superclass(ClassName(packageName(), name))
                                            .build()
                                }

                            }
            )
            .build()

    fun KClass<*>.buildActionsActions(): FileSpec {
        return FileSpec
                .builder(packageName(), actionsActionName())
                .addComment(generatedKotlinComment)
                .addType(
                        buildActionType(actionsActionName())
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

        return FileSpec
                .builder(packageName(), actionsImplName())
                .addType(
                        TypeSpec.classBuilder(actionsImplName())
                                .addSuperinterface(this)
                                .addPrimaryConstructorWithParams(
                                        listOf(
                                                ParamInfos("activity", actionActivityClass(), listOf(KModifier.PRIVATE)),
                                                ParamInfos("fragment", actionFragmentClass().nullable(), listOf(KModifier.PRIVATE))
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

    fun KMutableProperty<*>.initialValueName() = "${name}Initial"

    fun KClass<out ComponentView>.buildViewImplGen(): FileSpec {
        val thisActions = actions().map { it.jvmErasure }
        val thisFuncs = ownFuncs()
        val subComponentsMembers = subComponentMembers()
        val superComponentsFromAppNotPrivateMembers = superComponents().flatMap { it.propertyMember().filter { it.annotations.none { it is Private } } }
        return FileSpec
                .builder(packageName(), viewImplGenName())
                .apply {
                    thisActions.forEach {
                        addImport(it.packageName(), it.actionsActionName(), "treatAction")
                    }
                    if (subComponentsMembers.isNotEmpty()) {
//                        addImport("tech.skot.components", "ComponentViewImpl")
                        subComponentsMembers.forEach {
                            addImportClassName(it.returnType.componentView()!!.activityClassBound())
                            addImportClassName(it.returnType.componentView()!!.fragmentClassBound())
                        }
                    }

                }
                .addComment(generatedKotlinComment)
                .addType(
                        TypeSpec
                                .classBuilder(viewImplGenName())
                                .addModifiers(KModifier.ABSTRACT)
                                .apply {
                                    if (!isActualComponent()) {
                                        addTypeVariables(listOf(
                                                TypeVariableName("A", activityClassBound()),
                                                TypeVariableName("F", fragmentClassBound()),
                                                TypeVariableName("B", viewBindingClassName)
                                        ))
                                    }
                                }
                                .superclass(
                                        superView()
                                                .viewImplClassName()
                                                .parameterizedBy(activityClass(), fragmentClass(), bindingClass())
                                )
                                .addSuperclassConstructorParameter(
                                        (superComponentsFromAppNotPrivateMembers.map { it.name }).joinToString(", ")
                                )
                                .addPrimaryConstructorWithParams(
                                        subComponentsMembers.map {
                                            ParamInfos(it.name, it.returnType.viewImplClassName()!!, isVal = true, modifiers = listOf(KModifier.OVERRIDE))
                                        }
                                                +
                                                propertyMember().map {
                                                    if (it is KMutableProperty) {
                                                        ParamInfos(it.initialValueName(), it.returnType.asTypeName(), isVal = false)
                                                    } else {
                                                        ParamInfos(it.name, it.returnType.asTypeName(), modifiers = listOf(KModifier.OVERRIDE), isVal = true)
                                                    }

                                                }
                                                +
                                                superComponentsFromAppNotPrivateMembers.map {
                                                    ParamInfos(it.name, it.returnType.asTypeName(), isVal = false)
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
                                                                    .initializer("MutableSKLiveData(${it.initialValueName()})")
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
                                .addFunctions(
                                        thisFuncs
                                                .map {
                                                    FunSpec.builder(it.nowMethodName())
                                                            .addModifiers(KModifier.ABSTRACT)
                                                            .addParameters(
                                                                    it.functionParameters().map { ParameterSpec(it.name!!, it.type.asTypeName()) }
                                                            )
                                                            .build()
                                                }
                                )
                                .apply {
                                    if (subComponentsMembers.isNotEmpty() || thisActions.isNotEmpty()) {
                                        addFunction(
                                                FunSpec.builder("initWith")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("activity", activityClass())
                                                        .addParameter("fragment", fragmentClass().nullable())
                                                        .addParameter("binding", bindingClass())
                                                        .apply {
                                                            if (isActualComponent()) {
                                                                addModifiers(KModifier.FINAL)
                                                            }
                                                            subComponentsMembers
                                                                    .forEach {
//                                                                        val subCompType = "ComponentViewImpl<${it.returnType.componentView()!!.activityClassBound().simpleName},${it.returnType.componentView()!!.fragmentClassBound().simpleName}>"
                                                                        if (it.returnType.isCollectionOfComponentView()) {
                                                                            TODO("gérer les collections de sous composanst avec des méthodes abstract pour récupérer les bindings des éléments")
//                                                                            addStatement("${it.name}.forEach { it.initWith(activity, fragment, binding)}")
                                                                        } else {
                                                                            if (!it.hasAnnotation(LinkManually::class)) {
                                                                                addStatement("${it.name}.initWith(activity, fragment, binding.${it.name}${if (mapCoreCompViewAndroidView.containsKey(it.returnType.jvmErasure)) " as ${mapCoreCompViewAndroidView[it.returnType.jvmErasure]}" else ""})")
                                                                            }
                                                                        }

                                                                    }
                                                            thisActions.forEach {
                                                                addStatement("${it.actionsImplName().decapitalize()} = ${it.actionsImplName()}(activity, fragment)")
                                                            }
                                                        }
                                                        .addStatement("super.initWith(activity, fragment, binding)")
                                                        .build()
                                        )

                                        addFunctions(subComponentsMembers.filter {
                                            !it.returnType.isCollectionOfComponentView() && it.hasAnnotation(LinkManually::class)
                                        }.map {
                                            FunSpec.builder("link${it.name.capitalize()}")
                                                    .addParameter("binding", it.returnType.componentView()!!.bindingClass())
                                                    .beginControlFlow("${it.name}.let")
                                                    .addStatement("it.initWith(activity, fragment, binding)")
                                                    .addStatement("it.linkTo(lifeCycleOwner)")
                                                    .endControlFlow()
                                                    .build()
                                        })
                                    }
                                }
                                .apply {
                                    if (isActualComponent() && isScreenView()) {
                                        addFunction(
                                                FunSpec.builder("inflateBinding")
                                                        .addModifiers(KModifier.OVERRIDE, KModifier.FINAL)
                                                        .addParameter("layoutInflater", layoutInflaterClassName)
                                                        .addStatement("return ${bindingClassName().simpleName}.inflate(layoutInflater)")
                                                        .build()
                                        )
                                    }
                                }
                                .apply {

                                    val members = propertyMember()
                                    if (members.isNotEmpty() || subComponentsMembers.isNotEmpty()) {
                                        addFunction(
                                                FunSpec.builder("linkTo")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("lifecycleOwner", lifecycleOwnerClassName)
                                                        //appel des link des sous-composants
                                                        .apply {
                                                            if (isActualComponent()) {
                                                                addModifiers(KModifier.FINAL)
                                                            }

                                                            subComponentsMembers
                                                                    .forEach {
                                                                        if (it.returnType.isCollectionOfComponentView()) {
                                                                            addStatement("${it.name}.forEach { it.linkTo(lifecycleOwner)}\n")
                                                                        } else {
                                                                            if (!it.hasAnnotation(LinkManually::class)) {
                                                                                addStatement("${it.name}.linkTo(lifecycleOwner)\n")
                                                                            }
                                                                        }

                                                                    }
                                                        }
                                                        .addCode("super.linkTo(lifecycleOwner)\n")
                                                        .apply {
                                                            members.partition {
                                                                it is KMutableProperty
                                                            }.let {
                                                                it.second.forEach {
                                                                    addStatement("${it.onMethodName()}(${it.name})")
                                                                }
                                                                it.first.forEach {
                                                                    beginControlFlow("${it.ldName()}.setObserver(lifecycleOwner)")
                                                                    addStatement("${it.onMethodName()}(it)")
                                                                    endControlFlow()
                                                                }
                                                            }
//                                                            members
//                                                                    .forEach {
//                                                                        if (it is KMutableProperty) {
//                                                                            beginControlFlow("${it.ldName()}.observe(lifecycleOwner)")
//                                                                            addStatement("${it.onMethodName()}(it)")
//                                                                            endControlFlow()
//                                                                        } else {
//                                                                            addStatement("${it.onMethodName()}(${it.name})")
//                                                                        }
//                                                                    }
                                                        }
                                                        .build()
                                        )
                                    }
                                }

                                .apply {

                                    addFunctions(
                                            buildActionPostMessageFuncs(compActionName())
                                    )
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
                                                        .flatMap { actionsClass ->
                                                            actionsClass
                                                                    .buildActionPostMessageFuncs(actionsClass.actionsActionName())
                                                        }
                                        )
                                    }
                                    if (thisActions.isNotEmpty() || thisFuncs.isNotEmpty()) {


                                        addFunction(
                                                FunSpec.builder("treatAction")
                                                        .addModifiers(KModifier.OVERRIDE)
                                                        .addParameter("action", actionClassName)
                                                        .beginControlFlow("when (action)")
                                                        .apply {
                                                            thisFuncs
                                                                    .forEach {
                                                                        addStatement("is ${compActionName()}.${it.name.capitalize()} -> ${it.nowMethodName()}(${it.functionParameters().map { "action.${it.name}" }.joinToString(", ")})")
                                                                    }
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

    fun KClass<*>.buildActionPostMessageFuncs(actionName: String): List<FunSpec> =
            ownFuncs()
                    .map {
                        val funcParameters = it.functionParameters()
                        FunSpec.builder(it.name)
                                .addParameters(
                                        it.functionParameters().map {
                                            ParameterSpec.builder(it.name!!, it.type.asTypeName())
                                                    .build()
                                        }
                                )
                                .addModifiers(KModifier.OVERRIDE, KModifier.FINAL)
                                .addStatement("messages.post($actionName.${it.name.capitalize()}${
                                if (funcParameters.isNotEmpty()) {
                                    funcParameters.map { it.name }.joinToString(", ", "(", ")")
                                } else ""})")
                                .build()
                    }

    fun KClass<out ComponentView>.buildViewImplSkeleton(): FileSpec {
        val subComponentsMembers = subComponentMembers()
        return FileSpec.builder(packageName(), viewImplName())
                .addComment("//### First generated by SKot but then you can (have to) edit, the main constructor only will modifyed by generator if needed")
                .addType(
                        TypeSpec.classBuilder(viewImplName())
                                .apply {
                                    if (!isActualComponent()) {
                                        addTypeVariables(listOf(
                                                TypeVariableName("A", activityClassBound()),
                                                TypeVariableName("F", fragmentClassBound()),
                                                TypeVariableName("B", viewBindingClassName)
                                        ))
                                        addModifiers(KModifier.ABSTRACT)
                                    }
                                }
                                .superclass(
                                        ClassName(packageName(), viewImplGenName())
                                                .let {
                                                    if (!isActualComponent()) {
                                                        it.parameterizedBy(activityClass(), fragmentClass(), bindingClass())
                                                    } else {
                                                        it
                                                    }
                                                })
                                .addPrimaryConstructorWithParams(
                                        subComponentsMembers.map {
                                            ParamInfos(it.name, it.returnType.viewImplClassName()!!, isVal = false)
                                        }
                                                +
                                                allPropertyMember().map {
                                                    if (it is KMutableProperty) {
                                                        ParamInfos(it.initialValueName(), it.returnType.asTypeName(), isVal = false)
                                                    } else {
                                                        ParamInfos(it.name, it.returnType.asTypeName(), isVal = false)
                                                    }

                                                }
                                )
                                .addSuperclassConstructorParameter(
                                        (subComponentsMembers.map { it.name } + allPropertyMember().map {
                                            if (it is KMutableProperty) {
                                                it.initialValueName()
                                            } else {
                                                it.name
                                            }
                                        }).joinToString(", ")
                                )
                                .addFunctions(
                                        propertyMember()
                                                .map {
                                                    FunSpec.builder(it.onMethodName())
                                                            .addModifiers(KModifier.OVERRIDE)
                                                            .addParameter(it.name, it.returnType.asTypeName())
                                                            .addStatement(toDoToGenerate)
                                                            .build()
                                                }
                                )
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
                                            actualComponentsFromApp
                                                    .map {
                                                        FunSpec.builder(it.compName().decapitalize())
                                                                .addModifiers(KModifier.OVERRIDE)
                                                                .addParameters(
                                                                        (it.subComponentMembers() + it.allPropertyMember())
                                                                                .map {
                                                                                    ParameterSpec(it.name, it.returnType.asTypeName())
                                                                                }

                                                                )
                                                                .addStatement("return ${it.viewImplName()}(${
                                                                (it.subComponentMembers() + it.allPropertyMember())
                                                                        .map { "${it.name}${if (it.returnType.componentView() != null) " as ${it.returnType.viewImplClassNameName()}" else ""}" }
                                                                        .joinToString(", ")
                                                                })")
                                                                .build()
                                                    }
                                    )
                                    .build()
                    )
                    .apply {
                        actualComponents
                                .filter { it.packageName() != appPackageName }
                                .forEach {
                                    addImportClassName(it.viewImplClassName())
                                }
                    }
                    .build()


    fun KProperty<*>.ldName() = "${name}LD"
    fun KClass<*>.actionActivityClass() = mapActionsActivity.getOrDefault(this, baseActivity)
    fun KClass<*>.actionFragmentClass() = mapActionsFragment.getOrDefault(this, baseFragment)

    //TODO on peut éviter beaucoup de paramétrage en prenant les paramètres de la super classe et des interfaces si != base
    //(sans dépendance à view pour autant
    fun KClass<out ComponentView>.activityClassBound() = mapComponentActivity.getOrDefault(this, baseActivity)

    fun KClass<out ComponentView>.fragmentClassBound() = mapComponentFragment.getOrDefault(this, baseFragment)

    fun KClass<out ComponentView>.activityClass() = if (isActualComponent()) activityClassBound() else TypeVariableName("A")

    fun KClass<out ComponentView>.fragmentClass() = if (isActualComponent()) fragmentClassBound() else TypeVariableName("F")

    fun KClass<out ComponentView>.bindingClassName() = ClassName("$viewPackageName.databinding", "${compName()}Binding")
    fun KClass<out ComponentView>.bindingClass() = if (isActualComponent()) bindingClassName() else TypeVariableName("B")
}

fun TypeName.nullable() = this.copy(true)

fun KFunction<*>.nowMethodName() = "${name}Now"
fun KProperty<*>.onMethodName() = "on${name.capitalize()}"

fun KClass<*>.actionsActionName() = "${simpleName!!}Action"
fun KClass<*>.actionsActionClassName() = ClassName(packageName(), actionsActionName())

fun KClass<out ComponentView>.compActionName() = "${compName()}Action"

fun KCallable<*>.functionParameters() = parameters.filter { it.kind == KParameter.Kind.VALUE }


fun KClass<*>.actionsImplName() = "${simpleName!!}Impl"
fun KClass<*>.actionsImpClasslName() = ClassName(packageName(), actionsImplName())


fun KClass<out ComponentView>.viewImplName() = "${simpleName!!}Impl"

fun KClass<out ComponentView>.viewImplGenName() = "${viewImplName()}Gen"

fun KClass<out ComponentView>.viewImplClassName() = ClassName(packageName(), viewImplName())



