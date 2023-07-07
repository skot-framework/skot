import java.util.Locale

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("maven-publish")
    signing
}

group = Versions.group
version = Versions.version



kotlin {

    jvm()

    ios()

    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":modelcontract"))
                api("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                api("io.ktor:ktor-client-auth:${Versions.ktor}")
                api("io.ktor:ktor-client-logging:${Versions.ktor}")
            }
        }
        

        val androidMain by getting {
            dependencies {
                api("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
                api("io.ktor:ktor-client-okhttp:${Versions.ktor}")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}")
                api("io.ktor:ktor-client-okhttp:${Versions.ktor}")
                api("io.ktor:ktor-client-mock-jvm:${Versions.ktor}")
                api("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
            }

        }


        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.test.espresso:espresso-core:3.5.1")
                implementation("androidx.test:core-ktx:1.5.0")
                implementation("androidx.test.ext:junit-ktx:1.1.5")
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }

        val androidUnitTest by getting {
            dependencies {
               implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            }
        }

    }


}


android {
    defaultConfig {
        minSdk = Versions.Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileSdk = Versions.Android.compileSdk
    namespace = "tech.skot.model"



    packaging {
        if (gradle.startParameter.taskNames.any {
                it.uppercase(Locale.getDefault()).contains("ANDROIDTEST")
            }) {
            resources.excludes.add("META-INF/*")
        }
    }


}

dependencies {

    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
}

sqldelight {

    this.database("PersistDb") {
        packageName = "tech.skot.model.persist"
    }
    linkSqlite = false
}
