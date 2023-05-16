import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("unused")
public class KotlinProjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply(KtfmtConventionPlugin::class)
                apply(DetektConventionPlugin::class)
            }

            configure<KotlinJvmProjectExtension> { explicitApi() }

            tasks.withType<KotlinCompilationTask<*>>().configureEach {
                it.compilerOptions.apply { languageVersion.set(KotlinVersion.KOTLIN_2_0) }
            }
        }
    }
}
