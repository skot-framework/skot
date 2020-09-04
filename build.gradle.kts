buildscript {

    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.Android.plugin}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.29.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}