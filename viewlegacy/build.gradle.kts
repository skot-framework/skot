group = Versions.group
version = Versions.version

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}


dependencies {
    api("androidx.core:core:1.7.0")
    api("androidx.appcompat:appcompat:1.4.1")
    api("androidx.constraintlayout:constraintlayout:2.1.3")
    api("androidx.viewpager2:viewpager2:1.0.0")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("com.google.android.material:material:${Versions.Android.appcompat}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    androidTestImplementation(project(":viewlegacyTests"))
}


android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        compileSdk = Versions.Android.compileSdk
        targetSdk = Versions.Android.targetSdk
    }
    compileSdk = Versions.Android.compileSdk

    sourceSets {
        getByName("main").java.srcDirs("src/androidMain/kotlin")
        getByName("main").java.srcDirs("generated/androidMain/kotlin")
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
        getByName("androidTest").java.srcDirs("src/ttest/kotlin")
        getByName("androidTest").res.srcDirs("src/ttest/res")
    }

    buildFeatures {
        viewBinding = true
    }


}


kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }


    sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
    sourceSets["commonMain"].dependencies {
        api(project(":core"))
        api(project(":viewcontract"))
    }


    sourceSets["androidMain"].dependencies {
    }


}

if (!localPublication) {
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
}

