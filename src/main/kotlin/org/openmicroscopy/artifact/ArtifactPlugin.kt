package org.openmicroscopy.artifact

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.openmicroscopy.AdditionalArtifactsPlugin
import org.openmicroscopy.AdditionalRepositoriesPlugin
import org.openmicroscopy.FunctionalTestPlugin
import org.openmicroscopy.PublishingPlugin

class ArtifactPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        apply<AdditionalArtifactsPlugin>()
        apply<AdditionalRepositoriesPlugin>()
        apply<PublishingPlugin>()
        apply<FunctionalTestPlugin>()
    }
}