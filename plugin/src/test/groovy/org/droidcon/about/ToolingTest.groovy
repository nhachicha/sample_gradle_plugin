package org.droidcon.about

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.gradle.api.tasks.TaskInstantiationException
import static org.junit.Assert.assertTrue

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.junit.Rule
import org.junit.Before
import org.junit.After
import com.nabilhachicha.nativedependencies.utils.*
import static org.fest.assertions.api.Assertions.assertThat
import org.gradle.tooling.ProgressListener
import org.gradle.tooling.ProgressEvent
import java.io.File
import org.apache.commons.io.FileUtils
import org.junit.rules.TemporaryFolder


class DependenciesResolverTest {
    @Rule
    public TempGradleProject gradleProject = new TempGradleProject();

    ProjectConnection mConnection


    @Test
    public void testDefaultFlavours() {
        try {
            GradleConnector connector = GradleConnector.newConnector();

            //append DSL to this build
            gradleProject.tempGradleBuildFile.append """\
                    // defining flavours
                    android.productFlavors {
                        free
                        paid
                    }

                    """
            connector.forProjectDirectory(gradleProject.root);
            mConnection = connector.connect();

            // Configure the build
            BuildLauncher launcher = mConnection.newBuild();
            // this will generate properties files for both configuration
            launcher.forTasks("clean", "assembleFreeDebug" ,"assemblePaidDebug");

            // Run the build
            launcher.run();

            File freeFlavourProperties = new File(gradleProject.root.absolutePath +
                    File.separator + 'src' +
                    File.separator + 'free' +
                    File.separator + 'assets'+
                    File.separator + 'about.properties')

            File paidFlavourProperties = new File(gradleProject.root.absolutePath +
                    File.separator + 'src' +
                    File.separator + 'paid' +
                    File.separator + 'assets'+
                    File.separator + 'about.properties')


            assertThat(freeFlavourProperties).exists()
            assertThat(paidFlavourProperties).exists()

            mConnection?.close();

        } catch (Exception exception) {
            exception.printStackTrace()
        }
    }

    class TempGradleProject extends TemporaryFolder {
        final String GRADLE_TEMPLATE_DIRECTORY = "src"+File.separator+"test"+File.separator+"gradle_project_template"
        final String GRADLE_TEMPLATE_BUILD_FILE = "build.gradle"

        File tempGradleBuildFile

        @Override
        protected void before() throws Throwable {
            super.before();

            File templateDir = new File(GRADLE_TEMPLATE_DIRECTORY)

            def root = getRoot()
            FileUtils.copyDirectory(templateDir, root)

            tempGradleBuildFile = new File(root, GRADLE_TEMPLATE_BUILD_FILE)
        }
    }
}