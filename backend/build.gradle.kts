@file:Suppress("UnstableApiUsage")

import com.google.cloud.tools.jib.gradle.JibTask
import com.gorylenko.GenerateGitPropertiesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(spring.plugins.boot)
    alias(libs.plugins.convention.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.jib)
    alias(libs.plugins.git.properties)
    application
}

gitProperties { extProperty = "gitProperties" }

jib {
    to {
        setTags(project.provider { setOf("latest", "v$version") })
    }
    from {
        val baseImage = "eclipse-temurin:17.0.7_7-jre-alpine"
        val baseImageDigest =
            "sha256:5d1af3323cea5bd924875b911de740c1f07e68896331443a8add4ab2835a8430"
        image = "$baseImage@$baseImageDigest"
    }
    container {
        val gitProperties: Map<String, String> by project.extra
        creationTime.set(project.provider { gitProperties["git.commit.time"].toString() })
        ports = listOf("80")
        fun ociLabel(label: String, value: String) = "org.opencontainers.image.$label" to value
        labels.set(
            project.provider {
                mapOf(
                    ociLabel("revision", gitProperties["git.commit.id"].toString()),
                    ociLabel("version", "v$version"),
                )
            }
        )
    }
}

val generateGitProperties by
    tasks.getting(GenerateGitPropertiesTask::class) {
        // Always generate git properties
        outputs.upToDateWhen { false }
    }

tasks.withType<JibTask>().configureEach { dependsOn(generateGitProperties) }

testing { suites { withType<JvmTestSuite>().configureEach { useKotlinTest("1.8.10") } } }

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
