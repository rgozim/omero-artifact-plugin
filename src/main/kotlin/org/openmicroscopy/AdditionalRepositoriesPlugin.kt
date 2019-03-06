package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.repositories
import org.openmicroscopy.PluginHelper.Companion.resolveProperty
import java.net.URI

class AdditionalRepositoriesPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        repositories {
            // Add artifactory and maven repositories if defined
            closureOf<MavenArtifactRepository> {
                val remoteUrl = resolveProperty("MVN_REPOSITORY_URL", "mvnRepositoryUrl") ?: ""
                if (remoteUrl.length > 0) {
                    maven {
                        name = "maven-remote"
                        url = URI.create(remoteUrl)
                    }
                }
            }
            maven {
                name = "ome.maven"
                url = URI.create("https://artifacts.openmicroscopy.org/artifactory/maven/")
            }
            maven {
                name = "unidata"
                url = URI.create("https://artifacts.unidata.ucar.edu/repository/unidata-all/")
            }
        }
    }
}