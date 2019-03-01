package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.GroovySourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*


class FunctionalTestPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        plugins.withType(GroovyPlugin::class) {
            val sourceSets = the<JavaPluginConvention>().sourceSets

            val functionalTestSourceSet = sourceSets.create("functionalTest") {
                withConvention(GroovySourceSet::class) {
                    groovy.srcDir("src/functTest/groovy")
                }
                resources.srcDir("src/functTest/resources")
                compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
                runtimeClasspath += output + compileClasspath
            }

            val functionalTest = tasks.register<Test>("functionalTest") {
                description = "Runs the functional tests."
                group = "verification"
                testClassesDirs = functionalTestSourceSet.output.classesDirs
                classpath = functionalTestSourceSet.runtimeClasspath
                mustRunAfter(tasks.named("test"))
            }

            tasks["check"].dependsOn(functionalTest)
        }
    }
}