group = Versions.group
version = Versions.version

plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
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
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
//    implementation(project(":metacommon"))
    implementation(project(":viewcontract"))
    implementation(project(":modelcontract"))
    api("com.squareup:kotlinpoet:${Versions.kotlinpoet}")
    api(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}")
}