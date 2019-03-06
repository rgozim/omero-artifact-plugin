plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("com.google.guava:guava:27.0.1-jre")
}

gradlePlugin {
    plugins {
        register("plugin-publish-artifact-plugin") {
            id = "org.openmicroscopy.plugin-publish-artifact"
            implementationClass = "org.openmicroscopy.PublishingPlugin"
        }
    }
}

//     id("com.jfrog.artifactory") version "4.9.1"