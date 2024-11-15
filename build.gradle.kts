import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    id("java")
    kotlin("jvm") version "2.0.21"
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.bitvavo.jmh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-benchmark-runtime", version = "0.4.12")
    testImplementation(group = "org.openjdk.jmh", name = "jmh-core", version = "1.37")
    testImplementation(group = "org.openjdk.jmh", name = "jmh-generator-annprocess", version = "1.37")
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
}
