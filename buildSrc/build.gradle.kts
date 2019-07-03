plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "org.openmicroscopy"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenLocal()
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
            implementationClass = "org.openmicroscopy.AdditionalArtifactsPlugin"
        }
        register("functional-test-plugin") {
            id = "org.openmicroscopy.functional-test"
            implementationClass = "org.openmicroscopy.FunctionalTestPlugin"
        }
        register("plugin-publishing-plugin") {
            id = "org.openmicroscopy.plugin-publishing"
            implementationClass = "org.openmicroscopy.PluginPublishingPlugin"
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
