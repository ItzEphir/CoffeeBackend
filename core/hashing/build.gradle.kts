plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

group = "com.ephirium.coffee_backend.core"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.ktor.utils)
    implementation(libs.commons.codec)
}