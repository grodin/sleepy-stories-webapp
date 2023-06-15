@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.12.2"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.5"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

gitHooks {
    preCommit {
        from("#!/bin/sh") {
            """
            main_branch=main
            branch="$(git rev-parse --abbrev-ref HEAD)"
    
            if [ "${'$'}branch" = "${'$'}main_branch" ]; then
              echo "You can't commit directly to ${'$'}main_branch branch"
              exit 1
            fi
            """
                .trimIndent()
        }
    }
    commitMsg { conventionalCommits() }
    createHooks()
}

includeBuild("build-logic")

dependencyResolutionManagement {
    repositories { mavenCentral() }
    versionCatalogs {
        create("spring") { from(files(rootDir.resolve("gradle/spring.versions.toml"))) }
    }
}

rootProject.name = "sleepy-stories"

include(
    "backend",
    "frontend",
)
