plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

group = "com.ephirium.coffee_backend.core"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.core.log)
    implementation(projects.core.config)

    implementation(libs.kotlinx.datetime)
    implementation(libs.koin.core)
    implementation(libs.auth0.jwt)
}