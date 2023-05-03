import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(DetektPlugin::class)

            tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
                it.reports.html.apply {
                    required.set(true)
                    outputLocation.set(file("build/reports/detekt.html"))
                }
            }

            configure<DetektExtension> { config.setFrom(rootProject.files(DETEKT_CONFIG_FILE)) }
        }
    }

    companion object {
        const val DETEKT_CONFIG_FILE = "config/detekt/detekt.yml"
    }
}
