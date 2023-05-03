plugins {
    alias(spring.plugins.boot)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.doctor)
    application
}

ktfmt { kotlinLangStyle() }

dependencies {
    implementation(
        platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    )

    implementation(spring.starter.web)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) { useKotlinTest("1.8.10") }
    }
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

application { mainClass.set("com.omricat.sleepystories.server.MainKt") }