plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "org.openmicroscopy"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.9.3")
}

gradlePlugin {
    plugins {
        // Plugins for gradle plugins
        register("additional-artifacts-plugin") {
            id = "org.openmicroscopy.additional-artifacts"
            implementationClass = "org.openmicroscopy.artifact.AdditionalArtifactsPlugin"
        }
        register("additional-repositories-plugin") {
            id = "org.openmicroscopy.additional-repositories"
            implementationClass = "org.openmicroscopy.artifact.AdditionalRepositoriesPlugin"
        }
        register("functional-test-plugin") {
            id = "org.openmicroscopy.functional-test"
            implementationClass = "org.openmicroscopy.artifact.FunctionalTestPlugin"
        }
        register("plugin-project-plugin") {
            id = "org.openmicroscopy.plugin-project"
            implementationClass = "org.openmicroscopy.artifact.PluginProjectPlugin"
        }
        register("plugin-publishing-plugin") {
            id = "org.openmicroscopy.plugin-publishing"
            implementationClass = "org.openmicroscopy.artifact.PluginPublishingPlugin"
        }
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    description = "Creates a jar of java sources, classified -sources"
    archiveClassifier.set("sources")
    from(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].allSource)
}

tasks.named("assemble") {
    dependsOn(sourcesJar)
}
