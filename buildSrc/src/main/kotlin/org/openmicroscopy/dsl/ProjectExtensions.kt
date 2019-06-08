package org.openmicroscopy.dsl

import com.google.common.base.CaseFormat
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.credentials
import java.net.URI

class ProjectExtensions {
    companion object {
        fun Project.getRuntimeClasspathConfiguration(): Configuration? =
                configurations.findByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)

        fun Project.camelCaseName(): String {
            return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, name)
        }

        fun Project.createArtifactoryMavenRepo(): MavenArtifactRepository? {
            val artiUrl = resolveProperty("ARTIFACTORY_URL", "artifactoryUrl")
                    ?: return null

            return repositories.maven {
                name = "artifactory"
                url = URI.create(artiUrl)
                credentials {
                    username = resolveProperty("ARTIFACTORY_USER", "artifactoryUser")
                    password = resolveProperty("ARTIFACTORY_PASSWORD", "artifactoryPassword")
                }
            }
        }

        fun Project.createGitlabMavenRepo(): MavenArtifactRepository? {
            val gitlabUrl = resolveProperty("GITLAB_URL", "gitlabUrl")
                    ?: return null

            return repositories.maven {
                name = "gitlab"
                url = URI.create(gitlabUrl)
                credentials(HttpHeaderCredentials::class, Action {
                    // Token specified by
                    val jobToken = System.getenv("CI_JOB_TOKEN")
                    if (jobToken != null) {
                        name = "Job-Token"
                        value = jobToken
                    } else {
                        name = "Private-Token"
                        value = resolveProperty("GITLAB_TOKEN", "gitlabToken")
                    }
                })
            }
        }

        fun Project.createStandardMavenRepo(): MavenArtifactRepository? {
            val releasesRepoUrl =
                    resolveProperty("MAVEN_RELEASES_REPO_URL", "mavenReleasesRepoUrl")
            val snapshotsRepoUrl =
                    resolveProperty("MAVEN_SNAPSHOTS_REPO_URL", "mavenSnapshotsRepoUrl")

            val chosenUrl =
                    (if (hasProperty("release")) releasesRepoUrl else snapshotsRepoUrl) ?: return null

            return repositories.maven {
                url = URI.create(chosenUrl)
                name = "maven"
                credentials {
                    username = resolveProperty("MAVEN_USER", "mavenUser")
                    password = resolveProperty("MAVEN_PASSWORD", "mavenPassword")
                }
            }
        }

        fun Project.resolveProperty(envVarKey: String, projectPropKey: String): String? {
            val propValue = System.getenv(envVarKey)
            if (propValue != null) {
                return propValue
            }

            return findProperty(projectPropKey)?.toString()
        }
    }
}
