package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.openmicroscopy.extensions.OmeroExtension

class OmeroPlugin : Plugin<Project> {

    companion object {
        const val EXTENSION_OMERO: String = "omero"
    }

    override fun apply(project: Project): Unit = project.run {
        val omero = extensions.create(EXTENSION_OMERO, OmeroExtension::class, this)

    }


}