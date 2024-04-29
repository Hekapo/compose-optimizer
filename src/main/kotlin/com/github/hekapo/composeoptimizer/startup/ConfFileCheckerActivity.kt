package com.github.hekapo.composeoptimizer.startup

import com.github.hekapo.composeoptimizer.compose.Files
import com.github.hekapo.composeoptimizer.notifications.ConfFileCreationListener
import com.github.hekapo.composeoptimizer.settings.ConfFileManager
import com.github.hekapo.composeoptimizer.settings.PluginSettingsComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.io.File

class ConfFileCheckerActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val confFileManager = ConfFileManager(project)
        val projectBasePath = project.basePath ?: return
        val projectDir = File(projectBasePath)

        val confFile = projectDir.listFiles()?.find { it.isFile && it.extension == Files.ConfFileExt }
        val hasConfFile = confFile != null

        confFileManager.mergeSettingsAndConfFile()

        if (!hasConfFile) {
            Notifications.Bus.notify(
                Notification(
                    "Custom Notification Group",
                    "Configuration file missing",
                    "The '.conf' file is missing in the project root. <a href='create'>Create it now.</a>",
                    NotificationType.WARNING,
                    ConfFileCreationListener(projectDir) // Указываем слушателя
                ),
                project
            )
        }
    }
}
