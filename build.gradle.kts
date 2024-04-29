import java.util.*

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij)
    alias(libs.plugins.changelog)
    alias(libs.plugins.publish)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.jetbrains.com/maven")
    }
}

//kotlin {
//    jvmToolchain(18)
//}

intellij {
    pluginName.set(properties("pluginName"))
    version.set("2024.1")
//    type.set(properties("platformType"))
//    updateSinceUntilBuild.set(false)
    plugins.set(listOf("org.jetbrains.kotlin"))
}

changelog {
    groups.empty()
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set("241")
//        untilBuild.set(properties("pluginUntilBuild"))


        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest

//        pluginDescription.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
//            val start = "<!-- Plugin description -->"
//            val end = "<!-- Plugin description end -->"
//
//            with(it.lines()) {
//                if (!containsAll(listOf(start, end))) {
//                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
//                }
//                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
//            }
//        })
//
//        val changelog = project.changelog // local variable for configuration cache compatibility
//        // Get the latest available change notes from the changelog file
//        changeNotes.set(properties("pluginVersion").map { pluginVersion ->
//            with(changelog) {
//                renderItem(
//                    (getOrNull(pluginVersion) ?: getUnreleased())
//                        .withHeader(false)
//                        .withEmptySections(false),
//                    Changelog.OutputType.HTML,
//                )
//            }
//        })
    }

    runIde {
        val properties = readProperties(project.rootProject.file("local.properties"))
        val idePath = properties["studio.path"] as String
        if (idePath.isNotEmpty()) {
            ideDir.set(file(idePath))
        }
    }

    signPlugin {
        certificateChain.set(environment("CERTIFICATE_CHAIN"))
        privateKey.set(environment("PRIVATE_KEY"))
        password.set(environment("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(environment("PUBLISH_TOKEN"))
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(properties("pluginVersion").map {
            listOf(
                it.split('-').getOrElse(1) { "default" }.split('.').first()
            )
        })
    }

}
fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}
