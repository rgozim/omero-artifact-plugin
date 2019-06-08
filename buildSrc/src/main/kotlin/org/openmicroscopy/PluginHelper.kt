package org.openmicroscopy

import org.gradle.api.publish.maven.MavenPom

// Note: this should be copied to build.gradle.kts to ensure this plugin can publish itself
class PluginHelper {
    companion object {
        fun MavenPom.licenseGnu2() = licenses {
            license {
                name.set("GNU General Public License, Version 2")
                url.set("https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html")
                distribution.set("repo")
            }
        }
    }
}

