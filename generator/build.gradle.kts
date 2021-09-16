group = Versions.group
version = Versions.version

plugins {
    id("java-library")
    id("kotlin")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

configurations {
    all {
        attributes {
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    api(project(":viewcontract"))
    implementation(project(":modelcontract"))
    api("com.squareup:kotlinpoet:${Versions.kotlinpoet}")
    api(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}")
}