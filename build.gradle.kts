plugins {
    groovy
    `kotlin-dsl`
    `java-gradle-plugin`
    id("org.openmicroscopy.plugin-project")
}

group = "org.openmicroscopy"
version = "5.5.2-SNAPSHOT"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.9.3")
    implementation("org.ajoberstar:grgit:1.9.1") {
        setForce(true)
    }
    implementation("org.ajoberstar:gradle-git:1.7.1")
    implementation("org.ajoberstar:gradle-git-publish:0.3.3")

    api(files("$projectDir/buildSrc/build/libs/omero-plugin.jar"))
}

gradlePlugin {
    plugins {
        // Java/Groovy/Kotlin Project plugins
        register("project-plugin") {
            id = "org.openmicroscopy.project"
            implementationClass = "org.openmicroscopy.ProjectPlugin"
        }
        register("additional-repositories-plugin") {
            id = "org.openmicroscopy.additional-repositories"
            implementationClass = "org.openmicroscopy.AdditionalRepositoriesPlugin"
        }
        register("additional-artifacts-plugin") {
            id = "org.openmicroscopy.additional-artifacts"
            implementationClass = "org.openmicroscopy.AdditionalArtifactsPlugin"
        }
        register("publishing-plugin") {
            id = "org.openmicroscopy.publishing"
            implementationClass = "org.openmicroscopy.PublishingPlugin"
        }

        // Plugins for gradle plugins
        register("plugin-project-plugin") {
            id = "org.openmicroscopy.plugin-project"
            implementationClass = "org.openmicroscopy.PluginProjectPlugin"
        }
        register("plugin-publishing-plugin") {
            id = "org.openmicroscopy.plugin-publishing"
            implementationClass = "org.openmicroscopy.PluginPublishingPlugin"
        }

        // Used by both
        register("functional-test-plugin") {
            id = "org.openmicroscopy.functional-test"
            implementationClass = "org.openmicroscopy.FunctionalTestPlugin"
        }
        register("release-plugin") {
            id = "org.openmicroscopy.release"
            implementationClass = "org.openmicroscopy.ReleasePlugin"
        }
    }
}

// We need this to pull in compiled classes from the buildSrc jar.
tasks.jar {
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter {
            it.name.contains("omero-plugin")
        }.map {
            zipTree(it)
        }
    })
}
