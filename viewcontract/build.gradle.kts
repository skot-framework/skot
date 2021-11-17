plugins {
    kotlin("multiplatform")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

group = Versions.group
version = Versions.version


kotlin {
    jvm("jvm")
    ios {
        binaries {
            framework {
                baseName = "skViewContract"
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
    }

    sourceSets {
        val iosMain by getting {
        }
    }

}

multiplatformSwiftPackage {
    packageName("SkotViewContract")
    swiftToolsVersion("5.5")
    targetPlatforms {
        iOS { v("15") }
    }
}