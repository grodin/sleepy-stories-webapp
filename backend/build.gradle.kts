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
    to { setTags(project.provider { setOf("latest", "v$version") }) }
    from {
        val baseImage = "eclipse-temurin:17-jre-ubi9-minimal"
        val baseImageDigest =
            "sha256:41640b4547eca3bcf553ead39f4ca04a367d90cd77ee8a77d36194b093a9960e"
        image = "$baseImage@$baseImageDigest"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    container {
        val gitProperties: Map<String, String> by project.extra
        creationTime.set(project.provider { gitProperties["git.commit.time"].toString() })
        ports = listOf("8080")
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

version = "0.3.0"

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
