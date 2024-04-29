package com.github.hekapo.composeoptimizer.notifications

import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import java.io.File
import java.io.IOException
import javax.swing.event.HyperlinkEvent

class ConfFileCreationListener(private val projectDir: File) : NotificationListener {

    override fun hyperlinkUpdate(notification: Notification, event: HyperlinkEvent) {
        if (event.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            createConfFile(projectDir)
            Notifications.Bus.notify(
                Notification(
                    "Custom Notification Group",
                    "Configuration file created",
                    "A new '.conf' file has been created in the project root.",
                    NotificationType.INFORMATION
                )
            )
            notification.expire()
        }
    }

    private fun createConfFile(projectDir: File) {
        try {
            val confFile = File(projectDir, "compose_compiler_config.conf")
            confFile.createNewFile()
            confFile.writeText(
                """
                # Add your configuration parameters here
                """.trimIndent()
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

