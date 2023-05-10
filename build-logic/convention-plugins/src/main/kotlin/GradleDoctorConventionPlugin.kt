import com.osacky.doctor.DoctorPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

public class GradleDoctorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) { pluginManager.apply(DoctorPlugin::class) }
    }
}
