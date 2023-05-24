@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(spring.plugins.boot)
    alias(libs.plugins.convention.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    application
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) { useKotlinTest("1.8.10") }
    }
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

group = "com.omricat"

version = "0.1"

application { mainClass.set("com.omricat.sleepystories.server.MainKt") }

dependencies {
    implementation(projects.frontend)

    implementation(
        platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    )

    implementation(spring.starter.web)
    implementation(kotlin("reflect"))
    implementation(spring.jackson.kotlin)
    runtimeOnly(spring.devtools)

    implementation(libs.guava)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") }
}
