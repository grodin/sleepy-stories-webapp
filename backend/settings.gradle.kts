pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories { mavenCentral() }
    versionCatalogs {
        create("spring") { from(files(rootDir.resolve("gradle/spring.versions.toml"))) }
    }
}

plugins {}

rootProject.name = "backend"
