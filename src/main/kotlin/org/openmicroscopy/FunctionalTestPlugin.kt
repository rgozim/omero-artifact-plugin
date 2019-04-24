package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.HasConvention
import org.gradle.api.internal.IConventionAware
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.GroovySourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

class FunctionalTestPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        plugins.withType(GroovyPlugin::class) {
            val sourceSets = project.the<JavaPluginConvention>().sourceSets

            val functionalTestSourceSet = sourceSets.create("functionalTest") {
                val groovySrcSet = DslObject(this).convention.getPlugin(GroovySourceSet::class)
                groovySrcSet.groovy.srcDir("src/functTest/groovy")
                resources.srcDir("src/functTest/resources")
                compileClasspath += sourceSets["main"]!!.output + configurations["testRuntimeClasspath"]
                runtimeClasspath += output + compileClasspath
            }

            val functionalTest = tasks.create("functionalTest", Test::class) {
                description = "Runs the functional tests"
                group = "verification"
                testClassesDirs = functionalTestSourceSet.output.classesDirs
                classpath = functionalTestSourceSet.runtimeClasspath
            }

            tasks["check"].dependsOn(functionalTest)
        }
    }
}
