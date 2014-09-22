
package org.droidcon.about
import org.droidcon.about.AboutTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.gradle.api.tasks.TaskInstantiationException
import static org.junit.Assert.*

class BasicPluginTest {

    //TODO make this test more useful
    @Test (expected = TaskInstantiationException.class)
    public void testTaskCreation() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'about'
        def task = project.task('generateAbout', type: AboutTask)
        assertTrue(task instanceof AboutTask)
    }

}