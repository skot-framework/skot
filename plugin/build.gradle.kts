group = Versions.group
version = Versions.version

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("kotlinx-serialization")
    id("maven-publish")
    signing
}


dependencies {
    api("com.android.tools.build:gradle:${Versions.Android.gradle}")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    api("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")


    api("com.squareup:kotlinpoet:${Versions.kotlinpoet}")
}

configurations {
    all {
        attributes {
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
        }
    }
}


gradlePlugin {
    plugins {
        create("SkotModelContract") {
            id = "skot-modelcontract"
            implementationClass = "tech.skot.tools.gradle.PluginModelContract"
        }

        create("SkotViewContract") {
            id = "skot-viewcontract"
            implementationClass = "tech.skot.tools.gradle.PluginViewContract"
        }

        create("SkotViewModel") {
            id = "skot-viewmodel"
            implementationClass = "tech.skot.tools.gradle.PluginViewModel"
        }

        create("SkotModel") {
            id = "skot-model"
            implementationClass = "tech.skot.tools.gradle.PluginModel"
        }

        create("SkotViewLegacy") {
            id = "skot-viewlegacy"
            implementationClass = "tech.skot.tools.gradle.PluginViewLegacy"
        }

        create("SkotApp") {
            id = "skot-app"
            implementationClass = "tech.skot.tools.gradle.PluginApp"
        }

        create("SkotFeature") {
            id = "skot-feature"
            implementationClass = "tech.skot.tools.gradle.PluginFeature"
        }

        create("SkotTools") {
            id = "skot-tools"
            implementationClass = "tech.skot.tools.gradle.PluginTools"
        }

        create("Skot") {
            id = "skot-general"
            implementationClass = "tech.skot.tools.gradle.PluginGeneral"
        }

        create("SkotStarter") {
            id = "skot-starter"
            implementationClass = "tech.skot.tools.gradle.PluginStarter"
        }

        create("SkotLibraryContract") {
            id = "skot-library-contract"
            implementationClass = "tech.skot.tools.gradle.PluginLibraryContract"
        }

        create("SkotLibrary") {
            id = "skot-library"
            implementationClass = "tech.skot.tools.gradle.PluginLibrary"
        }

        create("SkotLibraryViewlegacy") {
            id = "skot-library-viewlegacy"
            implementationClass = "tech.skot.tools.gradle.PluginLibraryViewLegacy"
        }
    }
}

fun buildVersionsFile() {

    fun com.squareup.kotlinpoet.TypeSpec.Builder.addStringConst(
        name: String,
        value: String
    ): com.squareup.kotlinpoet.TypeSpec.Builder {
        return addProperty(
            com.squareup.kotlinpoet.PropertySpec.builder(
                name,
                String::class,
                com.squareup.kotlinpoet.KModifier.CONST
            )
                .initializer("\"$value\"")
                .build()
        )
    }

    fun com.squareup.kotlinpoet.TypeSpec.Builder.addIntConst(
        name: String,
        value: Int
    ): com.squareup.kotlinpoet.TypeSpec.Builder {
        return addProperty(
            com.squareup.kotlinpoet.PropertySpec.builder(
                name,
                Int::class,
                com.squareup.kotlinpoet.KModifier.CONST
            )
                .initializer(value.toString())
                .build()
        )
    }

    val file = com.squareup.kotlinpoet.FileSpec.builder("tech.skot", "Versions")
    val classBuilderCommon = com.squareup.kotlinpoet.TypeSpec.objectBuilder("Versions")
        .addStringConst("skot", Versions.version)
        .addStringConst("group", Versions.group)
        .addStringConst("serialization", Versions.serialization)
        .addStringConst("kotlinxDateTime", Versions.kotlinxDateTime)
        .addStringConst("ktor", Versions.ktor)
        .addStringConst("kotlin", Versions.kotlin)
        .addStringConst("kotlinCoroutines", Versions.kotlinCoroutines)
        .addStringConst("kotlinpoet", Versions.kotlinpoet)
        .addStringConst("sl4j", Versions.sl4j)
        .addIntConst("android_minSdk", Versions.Android.minSdk)
        .addIntConst("android_compileSdk", Versions.Android.compileSdk)
        .addIntConst("android_targetSdk", Versions.Android.targetSdk)
        .addStringConst("android_app_compat", Versions.Android.appcompat)




    file.addType(classBuilderCommon.build())
    file.build().writeTo(rootProject.projectDir.resolve("plugin/src/main/kotlin"))
}

buildVersionsFile()

val publication = getPublication(project)
publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set(project.name)
            description.set("${project.name} description")
            url.set("https://github.com/skot-framework/skot")
            licenses {
                license {
                    name.set("Apache 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("MathieuScotet")
                    name.set("Mathieu Scotet")
                    email.set("mscotet.lmit@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/skot-framework/skot.git")
                developerConnection.set("scm:git:ssh://github.com/skot-framework/skot.git")
                url.set("https://github.com/skot-framework/skot/tree/master")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        publication.signingKeyId,
        publication.signingKey,
        publication.signingPassword
    )
    this.sign(publishing.publications)
}