import com.github.gradle.node.yarn.task.YarnTask

plugins {
    java // Used just to bundle the frontend into a jar
    id("com.github.node-gradle.node") version "4.0.0"
}

node {
    version.set("20.1.0")
    download.set(true)
}

val buildTask =
    tasks.register<YarnTask>("buildFrontend") {
        description = "Build frontend with yarn"
        dependsOn(tasks.yarnSetup)
        args.addAll(
            "parcel",
            "build",
        )
//        args.addAll(
//            "--dist-dir",
//            "${buildDir}/frontend/static",
//        )
        inputs.dir(project.fileTree("src"))
        inputs.dir(project.fileTree("node_modules"))
        inputs.files("package.json", "tsconfig.json", ".parcelrc")
        outputs.dir(layout.buildDirectory.dir("frontend"))
    }

sourceSets { java { main { resources { srcDir(buildTask) } } } }

tasks.build { dependsOn(buildTask) }
