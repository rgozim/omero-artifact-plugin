package org.openmicroscopy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class OmeroExtension { /* do nothing */ }

class OmeroPlugin : Plugin<Project> {
    companion object {
        const val EXTENSION_OMERO = "foo"
    }

    override fun apply(project: Project): Unit = project.run {
        extensions.create<OmeroExtension>(EXTENSION_OMERO)
    }
}

