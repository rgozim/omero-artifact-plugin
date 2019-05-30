package org.openmicroscopy.extensions

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty

class OmeroExtension {

    private var name: String

    private var project: Project

    var databases: ListProperty<String>

    constructor(name: String, project: Project) {
        this.name = name
        this.project = project
        this.databases = project.objects.listProperty(String::class)
    }


}