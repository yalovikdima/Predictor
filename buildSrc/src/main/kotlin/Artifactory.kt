import groovy.lang.GroovyObject
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.delegateClosureOf
import org.gradle.kotlin.dsl.getPluginByName
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

val Project.Artifactory: ArtifactoryPluginConvention
    get() = ((this as? Project)?.convention
        ?: (this as HasConvention).convention).getPluginByName("artifactory")

fun Project.artifactory(
    configure: ArtifactoryPluginConvention.() -> Unit
): Unit = configure(Artifactory)

fun Project.initArtifactory() {
    plugins.apply("com.jfrog.artifactory")
    artifactory {
        setContextUrl(Publishing.contextUrl)
        publish(delegateClosureOf<PublisherConfig> {
            repository(delegateClosureOf<GroovyObject> {
                setProperty("repoKey", "libs-release-local")
                setProperty("username", repoUsername)
                setProperty("password", repoPassword)
            })
            defaults(delegateClosureOf<GroovyObject> {
                invokeMethod("publications", Publishing.publishingName)
                setProperty("publishArtifacts", true)
                setProperty("publishPom", true)
            })
        })
    }
}

fun Project.publishing(
    configure: PublishingExtension.() -> Unit
): Unit = (this as ExtensionAware).extensions.configure("publishing", configure)

fun Project.publishArtifacts(
    sourcesJar: TaskProvider<Jar>,
    configurations: ConfigurationContainer,
    project: Project,
    artifactId: String,
    addDependencies: Boolean,
    extraDependencies: List<String>,
    buildReleaseNotes: () -> Unit = {}
) {
    plugins.apply("maven-publish")

    buildReleaseNotes.invoke()

    publishing {
        publications {
            create(Publishing.publishingName, MavenPublication::class.java) {
                this.groupId = Publishing.predictorGroupId
                this.artifactId = artifactId
                this.version = Publishing.getPredictorVersion()

                this.artifact("${project.buildDir}/outputs/aar/${project.name}-release.aar")
                this.artifact(sourcesJar.get())

                this.pom.withXml {
                    asNode().appendNode("dependencies").let { dependencies ->
                        configurations.getDeps().forEach {

                            if (addDependencies) {
                                var needToSkip = false
                                if (it.artifactId == "predictorShared" || it.groupId.orEmpty()
                                        .startsWith("Predictor")
                                ) {
                                    needToSkip = true
                                }
                                if (!needToSkip) {
                                    val dependencyNode = dependencies.appendNode("dependency")
                                    dependencyNode.appendNode("groupId", it.groupId)
                                    dependencyNode.appendNode("artifactId", it.artifactId)
                                    dependencyNode.appendNode("version", it.version)
                                }
                            }
                        }

                        extraDependencies.forEach { dep ->
                            val dependencyNode = dependencies.appendNode("dependency")
                            dependencyNode.appendNode("groupId", Publishing.predictorGroupId)
                            dependencyNode.appendNode("artifactId", dep)
                            dependencyNode.appendNode("version", Publishing.getPredictorVersion())
                        }
                    }
                }
            }
        }
    }
}

class ArtifactDependency(
    val groupId: String?,
    val artifactId: String,
    val version: String?
)

fun ConfigurationContainer.getDeps(): List<ArtifactDependency> {
    return this.getByName("releaseCompileClasspath").resolvedConfiguration.firstLevelModuleDependencies.map {
        ArtifactDependency(it.moduleGroup, it.moduleName, it.moduleVersion)
    }
}