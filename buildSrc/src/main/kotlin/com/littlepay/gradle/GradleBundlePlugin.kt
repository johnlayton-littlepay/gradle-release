package com.littlepay.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RelativePath
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import java.io.FileOutputStream

open class LittlepayExtension(
    var gradleVersion: String = "8.12"
)

abstract class DownloadGradle : DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    init {
        group = "build"
        description = "Downloads the specified version of Gradle distribution"

        version.set("7.2")
        output.set(project.layout.buildDirectory.dir("temp/dist"))
    }

    @TaskAction
    fun action() {
        val url = "https://services.gradle.org/distributions/gradle-${version.get()}-bin.zip"
        project.uri(url).toURL().openStream().use {
            it.copyTo(FileOutputStream(project.layout.buildDirectory.file("temp/dist.zip").get().asFile))
        }
        project.copy {
            into(output.get())
            from(project.zipTree(project.layout.buildDirectory.file("temp/dist.zip").get())) {
                includeEmptyDirs = false
                include("gradle-${version.get()}/**")
                eachFile {
                    relativePath = RelativePath(true, *relativeSourcePath.segments.drop(1).toTypedArray())
                }
            }
        }
    }
}

class GradleBundlePlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        project.extensions.create<LittlepayExtension>("littlepay")
    }
}
