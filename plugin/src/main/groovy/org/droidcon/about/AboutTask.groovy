package org.droidcon.about

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import java.io.File

class AboutTask extends DefaultTask {
    @Input
    String flavor

    @TaskAction
    def exec () {
        getOutputFile().withWriter { out ->
            out.writeLine "commit=${getCommit()}"
            out.writeLine "author=${getAuthor()}"
            out.writeLine "flavour=${flavor}"
        }
    }

    @Input
    String getCommit () {
        "git rev-parse HEAD".execute([], project.rootDir).text.trim()
    }

    @Input
    String getAuthor () {
        "git log --format=%an -n 1 HEAD".execute([], project.rootDir).text.trim()
    }

    @OutputFile
    File getOutputFile () {
        new File ("${project.projectDir}/src/${flavor}/assets/${project.about.fileName}.${project.about.fileExtension}")
    }
}