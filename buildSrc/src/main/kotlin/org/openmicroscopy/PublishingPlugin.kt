package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*
import org.openmicroscopy.PluginHelper.Companion.camelCaseName
import org.openmicroscopy.PluginHelper.Companion.createArtifactoryMavenRepo
import org.openmicroscopy.PluginHelper.Companion.createGitlabMavenRepo
import org.openmicroscopy.PluginHelper.Companion.createStandardMavenRepo
import org.openmicroscopy.PluginHelper.Companion.licenseGnu2
import org.openmicroscopy.PluginHelper.Companion.safeAdd

class PublishingPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.run {
        apply<MavenPublishPlugin>()
    }

    private
    fun Project.configurePublishingExtension() {
        configure<PublishingExtension> {
            repositories {
                safeAdd(createArtifactoryMavenRepo())
                safeAdd(createGitlabMavenRepo())
                safeAdd(createStandardMavenRepo())
            }

            publications {
                create<MavenPublication>(camelCaseName()) {
                    plugins.withType<JavaPlugin> {
                        from(components["java"])
                        artifact(tasks.getByName("sourcesJar"))
                        artifact(tasks.getByName("javadocJar"))
                    }

                    plugins.withType<GroovyPlugin> {
                        artifact(tasks.getByName("groovydocJar"))
                    }

                    pom {
                        licenseGnu2()
                        afterEvaluate {
                            withXml {
                                val repositoriesNode = asNode().appendNode("repositories")
                                repositories.forEach {
                                    if (it is MavenArtifactRepository) {
                                        val repositoryNode = repositoriesNode.appendNode("repository")
                                        repositoryNode.appendNode("id", it.name)
                                        repositoryNode.appendNode("name", it.name)
                                        repositoryNode.appendNode("url", it.url)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}