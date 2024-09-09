plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktor)
}

group = "com.ephirium.coffee_backend.feature"
version = "0.0.1"

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
    implementation(projects.data.user)
    implementation(projects.data.password)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.ktor.server.core)
    implementation(libs.koin.core)
}