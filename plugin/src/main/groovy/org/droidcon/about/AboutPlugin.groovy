package org.droidcon.about

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.TaskInstantiationException
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

class AboutPlugin implements Plugin<Project> {
    void apply(Project project) {
        verifyRequiredPlugins project

        project.extensions.create("about", AboutExtension)
        // TODO known limitation if there is no Flavours the plugin will not apply to default src/main
        project.afterEvaluate {
            project.android.productFlavors.all { flavour ->
                if (flavour.hasProperty("enableAboutPlugin") && !flavour.ext.enableAboutPlugin) return

                def dynamicTaskName = "displayRevisionFoR${flavour.name}"

                project.tasks.find {
                    def pattern = ~/(?i)merge${flavour.name}.*Assets/
                    pattern.matcher(it.name).matches()
                }?.dependsOn project.tasks.create([name: "$dynamicTaskName", type: AboutTask], {
                    flavor = flavour.name
                })
            }
        }
    }

    // check if 'android' plugin is applied to the project
    private static void verifyRequiredPlugins(Project project) {
        if (!project.plugins.hasPlugin(AppPlugin) && !project.plugins.hasPlugin(LibraryPlugin)) {
            throw new TaskInstantiationException("'android' or 'android-library' plugin has to be applied before")
        }
    }
}