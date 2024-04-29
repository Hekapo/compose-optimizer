package com.github.hekapo.composeoptimizer.settings

import com.github.hekapo.composeoptimizer.compose.Files
import com.intellij.openapi.project.Project
import java.io.File

class ConfFileManager(project: Project) {

    private val settings by lazy { PluginSettingsComponent.getInstance() }

    private val projectBasePath by lazy { project.basePath?.let { File(it) } }
    private val confFile = projectBasePath?.listFiles()?.find { it.isFile && it.extension == Files.ConfFileExt }

    fun mergeSettingsAndConfFile() {
        confFile?.let {
            val existingLines = settings.stabilityChecksIgnoringClasses.split("\n").toMutableSet()

            confFile.inputStream().reader(Charsets.UTF_8).use { reader ->
                reader.readLines().forEach { line ->
                    if (line.firstOrNull()?.isLetter() == true) {
                        existingLines.add(line)
                    }
                }
            }

            val configContents = existingLines.joinToString("\n").also {
                println(it)
            }

            settings.stabilityChecksIgnoringClasses = configContents
        }
    }

    fun updateConfFile() {

    }
}
