plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
}

group = "com.ephirium.coffee_backend.data"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.core.log)
    implementation(projects.core.hashing)
    implementation(projects.core.token)

    implementation(libs.koin.core)
    implementation(libs.bundles.mongodb)
}