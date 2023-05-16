import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

group = "com.omricat.gradle"

version = libs.versions.plugin.convention.get()

kotlin { explicitApi() }

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(libs.ktfmt.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.detekt.cli)
    implementation(libs.doctor.gradlePlugin)
    implementation(libs.dependencyAnalysis.gradlePlugin)

    implementation(libs.kotlin.gradlePlugin)
}

val Provider<PluginDependency>.id: String
    get() = this.get().pluginId

val conventionPlugins: LibrariesForLibs.ConventionPluginAccessors
    get() = project.libs.plugins.convention

gradlePlugin {
    plugins {
        register("root") {
            id = conventionPlugins.root.id
            implementationClass = "RootProjectPlugin"
        }
        register("kotlin-jvm") {
            id = conventionPlugins.kotlin.jvm.id
            implementationClass = "KotlinProjectConventionPlugin"
        }
    }
}
